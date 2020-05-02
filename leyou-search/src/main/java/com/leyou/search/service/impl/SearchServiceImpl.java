package com.leyou.search.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;

import org.apache.commons.lang.StringUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.bouncycastle.math.raw.Nat;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.util.*;

/**
 * @ClassName:SearchServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/30
 * @TIME： 11:47
 * @Description: TODO
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 对象 转为json 的序列化用法 mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据关键字查询接口，可能需要分页、条件、聚合
     * 使用SearchRequest对象将要查询的条件封装成一个查询对象
     * @param searchRequest
     * @return
     */
    @Override
    public PageResult<Goods> search(SearchRequest searchRequest) {
        //这里查询的数据是从es 非关系型数据库中查询的
        if(StringUtils.isEmpty(searchRequest.getKey())){
            return null;
        }
        //创建查询条件构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //针对Goods 中all  字段进行查询  operator(Operator.AND)); 根据and交集进行查询
       queryBuilder.withQuery(QueryBuilders.matchQuery("all",
               searchRequest.getKey()).operator(Operator.AND));

       //过滤查询出来之后 所有需要拿到前端展示的数据
       queryBuilder.withSourceFilter(new FetchSourceFilter(
               new String[]{"id","skus","subTitle"},null));

       //分页
        queryBuilder.withPageable(PageRequest.of(
                searchRequest.getPage()-1,searchRequest.getSize()));


        Page<Goods> goods = this.goodsRepository.search(queryBuilder.build());

        return new PageResult<>(goods.getTotalElements(),
                (long) goods.getTotalPages(),
                goods.getContent());
    }

    /**
     * ES上传Goods 数据 。数据来源都是从数据中查询得到
     * @param spu
     * @return
     */
    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();
        //根据分类id查询分类名称
        List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(),
                spu.getCid2(), spu.getCid3()));
        //查询品牌信息
        Brand brand = this.brandClient.queryByBrandId(spu.getBrandId());
        //查询spuID 查询sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        //创建存商品价格集合
        List<Long> prices = new ArrayList<>();
        //创建一个封装List<Sku> 数据的容器
        List<Map<String,Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            //设置商品价格
            prices.add(sku.getPrice());

            //设置要展示的商品sku信息，使用map集合封装
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            //判断图片是否为空，空则设置为空字符串，否则分割图片，取第一张
            map.put("images",StringUtils.isNotBlank(sku.getImages())
                    ? "": StringUtils.split(sku.getImages(),",")[0]);
            map.put("price",sku.getPrice());
            skuList.add(map);
        });

        //根据分类id、generic 查询商品通用规格参数信息
        List<SpecParam> specParams = this.specificationClient.queryParamByGid(null, spu.getCid3(),
                null, true);
        //查询spu 商品的 详细规格参数信息
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySkuId(spu.getId());
        //商品通用规格参数
        Map<Long, Object> genericSpecMapper = MAPPER.readValue(spuDetail.getGenericSpec(),
                new TypeReference<Map<Long, Object>>() {
        });
        //查询商品特殊规格参数信息
        Map<Long, List<Object>> specialSpecMapper = MAPPER.readValue(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<Object>>>() {
        });

        //创建封装商品规格参数的map容器
        Map<String ,Object> map = new HashMap<>();

        //这里需要将商品规格参数 json格式的字符串 转出来，查询出商品的通用规格参数与商品的特殊规格参数
        //设置商品规格参数的信息，因为存在的商品参数规格信息都是json字符串，现在取需要反序列化
        specParams.forEach(specParam -> {
            //设置通用规格参数
            if(specParam.getGeneric()){
                String value = genericSpecMapper.get(specParam.getId()).toString();
                //这里需要判断是否有包含数值
                if(specParam.getNumeric()){
                    //设置商品价格区间进行展示
                    value = chooseSegment(value, specParam);
                }
                //设置通用规格参数
                map.put(specParam.getName(),value);
            }else {
                //设置特殊规格参数，不需要判断是否包含数值
                String value = specialSpecMapper.get(specParam.getId()).toString();
                map.put(specParam.getName(),value);
            }

        });


        //设置Goods对象所需要的参数
        goods.setId(spu.getId());
        goods.setAll(spu.getTitle()+""+brand.getName()+""+StringUtils.join(names,","));
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(brand.getId());
        goods.setCid1(goods.getCid1());
        goods.setCid2(goods.getCid2());
        goods.setCid3(goods.getCid3());
        goods.setCreateTime(goods.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuList));
        goods.setSpecs(map);

        return goods;
    }


    /**
     * 用来区分商品价格区间的方法
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

}

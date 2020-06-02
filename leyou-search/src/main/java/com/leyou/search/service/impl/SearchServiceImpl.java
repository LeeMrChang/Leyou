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
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;

import org.apache.commons.lang.StringUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.bouncycastle.math.raw.Nat;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.plugin.javascript.navig.Array;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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
    public SearchResult search(SearchRequest searchRequest) {
        //这里查询的数据是从es 非关系型数据库中查询的
        if(StringUtils.isEmpty(searchRequest.getKey())){
            return null;
        }
        //创建查询条件构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //针对Goods 中all  字段进行查询  operator(Operator.AND)); 根据and交集进行查询
        // 添加查询条件
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all",
                searchRequest.getKey()).operator(Operator.AND);
        queryBuilder.withQuery(basicQuery);

       //过滤查询出来之后 所有需要拿到前端展示的数据
       queryBuilder.withSourceFilter(new FetchSourceFilter(
               new String[]{"id","skus","subTitle"},null));

       //分页
        queryBuilder.withPageable(PageRequest.of(
                searchRequest.getPage()-1,searchRequest.getSize()));

        //创建分类 与品牌的桶
        String categoryAggName = "categories";
        String brandAggName = "brands";
        //使用ES API 创建桶
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //排序
        /*String sortBy = searchRequest.getSortBy();
        Boolean descending = searchRequest.getDescending();

        if(StringUtils.isNotBlank(sortBy)){
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy)
                    .order(descending ? SortOrder.DESC : SortOrder.ASC));
        }*/

        //执行搜索，获取搜索结果集，有分页、有聚合
        AggregatedPage<Goods> goods = (AggregatedPage<Goods>)
                this.goodsRepository.search(queryBuilder.build());

        //解析结果结果集
        List<Map<String, Object>> categories = getCategoryAggResult(goods.getAggregation(categoryAggName));
        //品牌结果集
        List<Brand> brands = getBrandAggResult(goods.getAggregation(brandAggName));

        System.out.println(categories.toString());

        // 判断分类聚合的结果集大小，等于1则聚合
        List<Map<String, Object>> specs = null;
        if (CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            specs = getParamAggResult((Long)categories.get(0).get("id"), basicQuery);
        }


        return new SearchResult(goods.getTotalElements(), (long) goods.getTotalPages(),
                goods.getContent(),categories,brands,specs);

    }

    /**
     * 创建索引
     * @param id
     */
    @Override
    public void createIndex(Long id) throws IOException {

        Spu spu = this.goodsClient.querySpuById(id);
        // 构建商品
        Goods goods = this.buildGoods(spu);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }

    /**
     * 删除索引
     * @param id
     */
    @Override
    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }

    /**
     * 聚合出规格参数过滤条件
     * @param id
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getParamAggResult(Long id, MatchQueryBuilder basicQuery) {
        // 创建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 基于基本的查询条件，聚合规格参数
        queryBuilder.withQuery(basicQuery);
        // 查询要聚合的规格参数
        List<SpecParam> params = this.specificationClient.queryParamByGid(null, id, null, true);
        // 添加聚合
        params.forEach(param -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs." + param.getName() + ".keyword"));
        });
        // 只需要聚合结果集，不需要查询结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));

        // 执行聚合查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());

        // 定义一个集合，收集聚合结果集
        List<Map<String, Object>> paramMapList = new ArrayList<>();
        // 解析聚合查询的结果集
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            // 放入规格参数名
            map.put("k", entry.getKey());
            // 收集规格参数值
            List<Object> options = new ArrayList<>();
            // 解析每个聚合
            StringTerms terms = (StringTerms)entry.getValue();
            // 遍历每个聚合中桶，把桶中key放入收集规格参数的集合中
            terms.getBuckets().forEach(bucket -> options.add(bucket.getKeyAsString()));
            map.put("options", options);
            paramMapList.add(map);
        }

        return paramMapList;
    }

    /**
     * 解析品牌 桶 的结果集方法
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        //处理聚合结果集
        Terms terms = (Terms) aggregation;
        //获取所有的品牌id
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        //定义一个品牌集合
        List<Brand> brands = new ArrayList<>();
        //解析所有的id桶。查询品牌
        /*buckets.forEach(bucket -> {
            Brand brand = this.brandClient.queryByBrandId(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });*/

        List<Brand> list = buckets.stream().map(bucket ->
                this.brandClient.queryByBrandId(bucket.getKeyAsNumber().longValue())
        ).collect(Collectors.toList());

        return list;
    }

    /**
     * 解析分类的 桶 的结果集方法
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        //处理聚合结果集
        Terms terms = (Terms) aggregation;
        /*//获取所有的品牌id
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        //定义一个品牌集合，搜集所有的品牌对象
        List<Map<String,Object>> categories = new ArrayList<>();
        List<Long> cids = new ArrayList<>();

        //解析所有id桶，查询品牌 根据品牌id查询到所有分类id
        buckets.forEach(bucket -> {
            cids.add(bucket.getKeyAsNumber().longValue());
        });

        //查询分类name
        List<String> names = this.categoryClient.queryNamesByIds(cids);

        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

        return categories;*/

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<Map<String,Object>> mapList = new ArrayList<>();
        buckets.forEach(bucket->{
            Map<String,Object> map = new HashMap<>();
            Long id = bucket.getKeyAsNumber().longValue();
            List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(id));
            map.put("id",id);
            map.put("name",names.get(0));
            mapList.add(map);
        });
        return mapList;
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
            map.put("image",StringUtils.isNotBlank(sku.getImages()) ?
                            StringUtils.split(sku.getImages(),",")[0]:""
                    );
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

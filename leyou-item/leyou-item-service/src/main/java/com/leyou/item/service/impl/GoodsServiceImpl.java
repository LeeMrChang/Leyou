package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:GoodsServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:55
 * @Description: TODO
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 商品 spu 的按条件 且分页查询
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //按照关键字查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        //按照是否上下架状态查询
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //封装分页结果集
        PageHelper.startPage(page,rows);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>();

        //这是最后需要返回的结果集
        List<SpuBo> list = new ArrayList<>();
        //拼接品牌名称与分类名称
        spus.forEach(spu -> {
            SpuBo dto = new SpuBo();
            //将spu 与spuDto共同属性赋值到  spuDto中
            BeanUtils.copyProperties(spu,dto);
            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            dto.setBname(brand.getName());

            //查询分类名称。  这里分类涉及多级分类，需要查询多个分类id
            List<String> names= this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1()
                    ,spu.getCid2(),
                    spu.getCid3()));
            //将string 集合添加进对象 已“-” 隔开
            dto.setCname(StringUtils.join(names,"->"));

            list.add(dto);
        });

        return new PageResult<>(pageInfo.getTotal(),list);
    }

    /**
     * 新增商品信息
     * @param spuBo
     */
    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {

        //先新增spu的数据
        //将主键id设置为空，为了防止故意注入数据
        spuBo.setId(null);
        //默认新增的商品为上架状态
        spuBo.setSaleable(true);
        //默认新增商品为有效状态
        spuBo.setValid(true);
        //新增商品创建时间为当前时间
        spuBo.setCreateTime(new Date());
        //最后创建时间需要与创建时间一致
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);


        //再新增spuDetail 商品描述信息的数据
        SpuDetail detail = spuBo.getSpuDetail();
        detail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(detail);

        //最后新增商品sku 就是商品规格与商品参数的数据
        List<Sku> skus = spuBo.getSkus();
        skus.forEach(sku -> {
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(spuBo.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //再新增商品库存数据
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });

    }
}

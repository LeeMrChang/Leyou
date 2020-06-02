package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import com.sun.xml.internal.ws.resources.SenderMessages;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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

    @Autowired
    private AmqpTemplate amqpTemplate;

    static  final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);



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
        saveSkuAndStock(spuBo);

        //新增商品需要需要发送消息队列
        sendMessage(spuBo.getId(),"insert");

    }

    /**
     * 根据sku_id 查询回显sku  商品规格参数信息
     * @param skuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySkuId(Long skuId) {
        return this.spuDetailMapper.selectByPrimaryKey(skuId);
    }

    /**
     * 根据spuid 查询sku 商品详情的 集合数据
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(sku1 -> {
            //查询设置商品库存信息
            Stock stock = this.stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });

        return skus;
    }

    /**
     * 更新商品信息
     * @param spubo
     */
    @Override
    public void updateGoods(SpuBo spubo) {

        //先根据spu_id查询到对应的skus 再查到对应stock
        Sku sku = new Sku();
        sku.setSpuId(spubo.getId());
        List<Sku> skus = this.skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(skus)){
            skus.forEach(sku1 -> {
                //先根据sku id 删除库存信息
                this.stockMapper.deleteByPrimaryKey(sku1.getId());
                //删除sku
                this.skuMapper.delete(sku);
            });
        }


        //更新sku
        saveSkuAndStock(spubo);

        //更新spu 与 spuDetail信息
        //设置防止恶意注入不良信息
        spubo.setCreateTime(null);
        spubo.setLastUpdateTime(new Date());
        spubo.setValid(null);
        spubo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spubo);

        //更新spuDetail信息
        this.spuDetailMapper.updateByPrimaryKeySelective(spubo.getSpuDetail());

        //修改的时候也需要发送消息通知商品修改了
        sendMessage(spubo.getId(),"update");

    }

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据skuId 查询sku对象
     * @param id
     * @return
     */
    @Override
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增商品sku
     * @param spuBo
     */
    public void saveSkuAndStock(SpuBo spuBo){
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

    /**
     * 发送消息的封装方法 根据商品id 同步商品数据 类型是修改还是新增
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }
}

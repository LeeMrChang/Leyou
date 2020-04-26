package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @ClassName:GoodsService
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:54
 * @Description: TODO
 */
public interface GoodsService {
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySkuId(Long skuId);

    List<Sku> querySkusBySpuId(Long spuId);

    void updateGoods(SpuBo spubo);
}

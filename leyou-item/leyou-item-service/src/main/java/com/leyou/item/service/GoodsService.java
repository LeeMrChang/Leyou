package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.SpuDto;

/**
 * @ClassName:GoodsService
 * @Author：Mr.lee
 * @DATE：2020/04/24
 * @TIME： 10:54
 * @Description: TODO
 */
public interface GoodsService {
    PageResult<SpuDto> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);
}

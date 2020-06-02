package com.leyou.goods.service;

import java.util.Map;

/**
 * @ClassName:GoodsService
 * @Author：Mr.lee
 * @DATE：2020/05/18
 * @TIME： 21:05
 * @Description: TODO
 */
public interface GoodsService {
    void createHtml(Long id);

    void deleteHtml(Long id);

    Map<String, Object> loadModel(Long id);
}

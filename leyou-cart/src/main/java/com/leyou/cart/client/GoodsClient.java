package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:GoodsClient
 * @Author：Mr.lee
 * @DATE：2020/05/20
 * @TIME： 16:14
 * @Description: TODO
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}

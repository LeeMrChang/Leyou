package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:GoodsClient
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:05
 * @Description: TODO
 *  @FeignClient(value = "item-service") 这样就可以远程调用 微服务中的接口了
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi{
}

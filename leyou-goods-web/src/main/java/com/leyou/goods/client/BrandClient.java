package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:BrandClient
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:16
 * @Description: TODO
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {


}

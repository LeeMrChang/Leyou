package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:CategoryClient
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:17
 * @Description: TODO
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}

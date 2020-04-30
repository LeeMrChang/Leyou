package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:SpecificationClient
 * @Author：Mr.lee
 * @DATE：2020/04/29
 * @TIME： 17:15
 * @Description: TODO
 */
@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}

package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName:UserClient
 * @Author：Mr.lee
 * @DATE：2020/05/19
 * @TIME： 15:39
 * @Description: TODO
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}

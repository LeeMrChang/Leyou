package com.leyou.user.api;

import com.leyou.user.pojo.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName:UserApi
 * @Author：Mr.lee
 * @DATE：2020/05/19
 * @TIME： 15:37
 * @Description: TODO
 */
@RequestMapping
public interface UserApi {

    @GetMapping("query")
    public User queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );
}

package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @ClassName:UserService
 * @Author：Mr.lee
 * @DATE：2020/05/18
 * @TIME： 15:12
 * @Description: TODO
 */
public interface UserService {
    Boolean checkData(String data, Integer type);

    Boolean sendVerifyCode(String phone);

    Boolean register(User user, String code);

    User queryUser(String username, String password);
}

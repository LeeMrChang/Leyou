package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:AuthServiceImpl
 * @Author：Mr.lee
 * @DATE：2020/05/19
 * @TIME： 15:23
 * @Description: TODO
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    /**
     * 登录校验，并生成 token
     * @param username
     * @param password
     * @return
     */
    @Override
    public String authentication(String username, String password) {

        try {
            //调用微服务进行查询
            User user = this.userClient.queryUser(username, password);

            // 如果查询结果为null，则直接返回null
            if (user == null) {
                return null;
            }

            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    this.properties.getPrivateKey(), this.properties.getExpire());

            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

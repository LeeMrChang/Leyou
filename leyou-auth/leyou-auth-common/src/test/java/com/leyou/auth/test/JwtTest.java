package com.leyou.auth.test;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @ClassName:JwtTest
 * @Author：Mr.lee
 * @DATE：2020/05/19
 * @TIME： 15:02
 * @Description: TODO
 */
public class JwtTest {

    private static final String pubKeyPath = "D:\\jwt\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\jwt\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4OTg3MjgyOX0.KT4c1rSrz1-M1sjkpK-vC1wIodQm01P3gYFvT9Tp5AzC4A2s3ZYbl3RVFjzKwJFpH0_KXwKiL2T7Ntgp00x3z6ZvWvPwfbOtCMALHEm_uUJq2bVV-ESRqNaznl6t0NyKa6RRBPaEKPdIotjTvSHQekWNYuarmOWfDySOBRsuyFc";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}

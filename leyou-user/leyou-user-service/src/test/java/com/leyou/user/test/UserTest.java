package com.leyou.user.test;

import com.leyou.user.LeyouUserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName:UserTest
 * @Author：Mr.lee
 * @DATE：2020/05/18
 * @TIME： 16:01
 * @Description: TODO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LeyouUserApplication.class)
public class UserTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test01(){
        this.redisTemplate.opsForValue().set("yyy","xxx");
        String yyy = this.redisTemplate.opsForValue().get("yyy");
        System.out.println(yyy);
    }
}

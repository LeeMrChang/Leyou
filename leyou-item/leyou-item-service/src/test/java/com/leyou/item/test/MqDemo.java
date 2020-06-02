package com.leyou.item.test;

import com.leyou.LeyouItemApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName:MqDemo
 * @Author：Mr.lee
 * @DATE：2020/05/18
 * @TIME： 20:43
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouItemApplication.class)
public class MqDemo {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSend() throws InterruptedException {
        String msg = "hello, Spring boot amqp";
        this.amqpTemplate.convertAndSend("spring.test.exchange","a.b", msg);
        // 等待10秒后再结束
        Thread.sleep(10000);
    }
}

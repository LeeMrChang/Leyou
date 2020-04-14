package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName:LeyouItemApplication
 * @Author：Mr.lee
 * @DATE：2020/04/13
 * @TIME： 12:08
 * @Description: TODO
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouItemApplication.class);
    }
}

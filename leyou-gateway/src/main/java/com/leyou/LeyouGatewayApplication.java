package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @ClassName:LeyouGatewayApplication
 * @Author：Mr.lee
 * @DATE：2020/04/13
 * @TIME： 11:42
 * @Description: TODO
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy  // 开启zuul网关服务
public class LeyouGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouGatewayApplication.class);
    }
}

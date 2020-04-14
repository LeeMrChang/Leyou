package com.leyou.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName:LeyouCorsConfiguration
 * @Author：Mr.lee
 * @DATE：2020/04/14
 * @TIME： 22:01
 * @Description: TODO
 */
@Configuration
public class LeyouCorsConfiguration {
    /**
     * 注入CORS解决跨域问题的配置类
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        //初始化cors配置
        CorsConfiguration configuration = new CorsConfiguration();

        //允许跨域的域名，如果要携带cookie不能写*.*
        configuration.addAllowedOrigin("http://manage.leyou.com");
        //允许携带cookie
        configuration.setAllowCredentials(true);
        //代表所有请求方式
        configuration.addAllowedMethod("*");
        //允许携带任何头信息
        configuration.addAllowedHeader("*");


        //初始化cors配置对象
        UrlBasedCorsConfigurationSource corsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        //
        corsConfigurationSource.registerCorsConfiguration("/**",configuration);


        //返回CORSFilter实例，参数：cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }

}

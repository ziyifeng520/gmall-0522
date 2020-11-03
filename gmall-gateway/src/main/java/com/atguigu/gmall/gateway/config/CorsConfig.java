package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        //初始化Cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();

        //允许的域
        configuration.addAllowedOrigin("http://manager.gmall.com");
        configuration.addAllowedOrigin("localhost:1000");
        //允许的头信息
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        //是否允许携带Cookie信息
        configuration.setAllowCredentials(true);

        //添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);


    }
}

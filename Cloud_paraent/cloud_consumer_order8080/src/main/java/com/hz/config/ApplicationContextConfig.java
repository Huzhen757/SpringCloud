package com.hz.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Bean()
    // @LoadBalanced // 开启restTemplate的负载均衡功能(否则order服务不能访问Service Provider)
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}

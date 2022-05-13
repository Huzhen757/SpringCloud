package com.hz.sentinel_lb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // 激活OpenFeign
public class SentinelLBOrder8402 {

    public static void main(String[] args) {
        SpringApplication.run(SentinelLBOrder8402.class, args);

    }
}

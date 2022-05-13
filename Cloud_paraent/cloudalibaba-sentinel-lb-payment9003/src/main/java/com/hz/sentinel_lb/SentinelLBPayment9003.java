package com.hz.sentinel_lb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SentinelLBPayment9003 {

    public static void main(String[] args) {
        SpringApplication.run(SentinelLBPayment9003.class, args);
    }
}

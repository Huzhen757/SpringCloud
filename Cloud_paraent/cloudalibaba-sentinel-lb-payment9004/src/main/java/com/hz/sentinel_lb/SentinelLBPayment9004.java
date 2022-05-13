package com.hz.sentinel_lb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SentinelLBPayment9004 {
    public static void main(String[] args) {
        SpringApplication.run(SentinelLBPayment9004.class, args);
    }
}

package com.hz.orderZk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.hz")
@EnableDiscoveryClient
public class OrderZkMain80 {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OrderZkMain80.class, args);

    }
}

package com.hz.orderMain;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.hz")
@EnableDiscoveryClient
public class OrderConsulMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OrderConsulMain.class, args);

    }
}

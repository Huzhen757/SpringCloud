package com.hz;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
public class HystrixOrderMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HystrixOrderMain.class, args);

    }
}

package com.hz.openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients // 表示当前类是一个Feign客户端
public class OrderMainFeign80 {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OrderMainFeign80.class, args);

    }
}

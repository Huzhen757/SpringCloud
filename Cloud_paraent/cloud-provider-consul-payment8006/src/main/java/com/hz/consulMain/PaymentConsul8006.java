package com.hz.consulMain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.hz")
@EnableDiscoveryClient
public class PaymentConsul8006 {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(PaymentConsul8006.class, args);


    }
}

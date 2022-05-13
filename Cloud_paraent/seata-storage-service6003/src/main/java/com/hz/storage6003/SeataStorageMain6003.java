package com.hz.storage6003;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.hz.storage6003.dao")
@EnableDiscoveryClient
@EnableFeignClients
public class SeataStorageMain6003 {

    public static void main(String[] args) {

        SpringApplication.run(SeataStorageMain6003.class, args);
    }

}

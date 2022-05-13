package com.hz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 使用自己配置的数据源配置类(Seata), 不使用默认配置的数据源配置类(Druid)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
public class SeataOrderMain6001 {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderMain6001.class, args);
    }

}

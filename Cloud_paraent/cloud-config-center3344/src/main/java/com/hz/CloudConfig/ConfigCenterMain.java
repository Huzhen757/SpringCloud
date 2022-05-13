package com.hz.CloudConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

// Config服务器端
@SpringBootApplication
@EnableConfigServer
public class ConfigCenterMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain.class, args);
    }

}

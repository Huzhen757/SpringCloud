package com.hz.openfeign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
   // OpenFeign的日志增强：Full表示打印全部信息，除了HEADERS中定义的头信息之外，还有请求和响应的正文以及元数据
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}

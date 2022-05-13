package com.hz.nacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NacosOrderController {

    // public final static String URL = "";

    @Autowired
    private RestTemplate restTemplate;
    // 实现配置和代码的分离 直接读取配置文件去获取服务提供者的URL
    @Value(("${service-url.nacos-user-service}"))
    private String serverURL;

    @GetMapping("/consumer/nacos/payment/{id}")
    public String PaymentInfo(@PathVariable("id") Integer id){

        return restTemplate.getForObject(serverURL + "/nacos/payment/"+id, String.class);
    }
}

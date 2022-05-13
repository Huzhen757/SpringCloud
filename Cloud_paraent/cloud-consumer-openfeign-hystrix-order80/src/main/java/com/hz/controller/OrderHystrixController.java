package com.hz.controller;

import com.hz.service.OrderHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "GlobalFallbackMethod")
public class OrderHystrixController {

    @Resource
    private OrderHystrixService service;

    @GetMapping("/consumer/payment/hystrix/success/{id}")
    @HystrixCommand // 没有特别指明就是用统一的服务降级DefaultProperties
    public String paymentInfo_success(@PathVariable("id") Integer id){
        // int i=10/0;
        String res = service.paymentInfo_success(id);
        return res;
    }
    // 设置客户端运行时间的上限，超过2s或者程序运行错误执行服务降级paymentInfoTimeoutHandler
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
    public String paymentInfo_timeout(@PathVariable("id") Integer id){
        // int i = 10 / 0;
        String res = service.paymentInfo_timeout(id);
        return res;

    }

    public String paymentInfoTimeoutHandler(Integer id){
        return "openfeign-hystrix-order80,client server run time over limit or runTime Exception o(-<>-)o";
    }

    // 全局服务降级GlobalFallbackMethod
    public String GlobalFallbackMethod(){
        return "GlobalFallback Method running...";
    }



}

package com.hz.hystrix.controller;


import com.hz.hystrix.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentHystrixController {

    @Autowired
    private PaymentHystrixService service;

    @Value("${server.port}")
    private String serverPort;
    // ====服务降级
    @GetMapping("/payment/hystrix/success/{id}")
    public String paymentInfo_success(@PathVariable("id") Integer id){
        String res = service.paymentInfo_success(id);
        log.info("****res: " +res);
        return res + "当前端口：" + serverPort;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id){
        String res = service.paymentInfo_timeout(id);
        log.info("****res: " +res);
        return res + "当前端口：" + serverPort;
    }

    // ====服务熔断
    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        String res = service.paymentCircuitBreaker(id);
        log.info("****service breaker: " + res);
        return res;
    }

}

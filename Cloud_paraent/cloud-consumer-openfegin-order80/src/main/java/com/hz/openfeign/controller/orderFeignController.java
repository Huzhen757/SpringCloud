package com.hz.openfeign.controller;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.openfeign.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@Slf4j
public class orderFeignController {

    @Resource
    private PaymentFeignService service;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return service.getPaymentById(id);
    }

    @GetMapping("/consumer/payment/getAll")
    public CommonResult<Payment> getAllPayment(){
        return service.getAllPayment();

    }

    // 使用RequestBody注解表示使用json数据进行传输
    @PostMapping("/consumer/payment/save")
    public CommonResult save(@RequestBody Payment payment){
        CommonResult res = service.save(payment);
        log.info("插入记录数：" + res);
        return res;
    }

    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        // openfeign-ribbon 客户端默认等待1s
        return service.paymentFeignTimeout();
    }
}

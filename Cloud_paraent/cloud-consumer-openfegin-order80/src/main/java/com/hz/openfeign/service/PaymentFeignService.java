package com.hz.openfeign.service;


import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE") // 获取哪个微服务的地址
public interface PaymentFeignService {
    // 注意这里返回值类型必须与需要调用的某个微服务的该方法泛型保持一致
    @GetMapping("/payment/get/{id}") // 对应该方法的uri
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    @GetMapping("/payment/selectall")
    CommonResult getAllPayment();

    @PostMapping("/payment/save")
    CommonResult save(@RequestBody Payment payment);


    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout();

}

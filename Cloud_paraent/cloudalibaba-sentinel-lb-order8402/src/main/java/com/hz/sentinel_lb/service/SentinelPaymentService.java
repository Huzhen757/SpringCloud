package com.hz.sentinel_lb.service;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.sentinel_lb.service.impl.SentinelPaymentServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// 指明调用哪一个微服务
@FeignClient(value = "cloudalibaba-sentinel-lb-provider", fallback = SentinelPaymentServiceImpl.class)
public interface SentinelPaymentService {
    // 与对应的微服务controller方法保持一致 方法体去掉即可
    @GetMapping(value = "/sentinel/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id);

}

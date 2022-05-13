package com.hz.service;

import com.hz.service.impl.OrderFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// Hystrix之通配服务降级FeignFallback
@Component
// @FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = OrderFallbackService.class)
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT")
public interface OrderHystrixService {

    @GetMapping("/payment/hystrix/success/{id}")
    public String paymentInfo_success(@PathVariable("id") Integer id);

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_timeout(@PathVariable("id") Integer id);


}

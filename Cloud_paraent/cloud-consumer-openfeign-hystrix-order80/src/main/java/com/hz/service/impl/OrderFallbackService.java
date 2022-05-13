package com.hz.service.impl;

import com.hz.service.OrderHystrixService;
import org.springframework.stereotype.Component;

@Component
public class OrderFallbackService implements OrderHystrixService {
    @Override
    public String paymentInfo_success(Integer id) {
        return "OrderFallbackService paymentInfo_success exec...Server probably is died";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "OrderFallbackService paymentInfo_timeout exec...Server probably is died";
    }
}

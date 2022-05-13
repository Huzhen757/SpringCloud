package com.hz.sentinel_lb.controller;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
@Slf4j
public class SentinelLBController9004 {

    @Value("${server.port}")
    public String serverPort;

    public static HashMap<Long, Payment> map = new HashMap<>();

    static{
        map.put(2001L, new Payment(78L, UUID.randomUUID().toString()));
        map.put(2002L, new Payment(79L, UUID.randomUUID().toString()));
        map.put(2003L, new Payment(80L, UUID.randomUUID().toString()));
        map.put(2004L, new Payment(81L, UUID.randomUUID().toString()));
    }

    @GetMapping(value = "/sentinel/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id){
        log.info("this is Sentinel+LB+OpenFeign+fallback payment 9004");
        Payment payment = map.get(id);
        return new CommonResult<>(200, "sentinel+lb, select from mysql, serverPort: " + serverPort, payment);
    }
}

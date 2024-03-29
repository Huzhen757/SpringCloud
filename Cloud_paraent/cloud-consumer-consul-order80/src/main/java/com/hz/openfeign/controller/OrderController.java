package com.hz.openfeign.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderController {

    public final static String INVOKE_URL = "http://cloud-provider-payment-consul8006";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/consul")
    public String orderConsul(){
        String forObject = restTemplate.getForObject(INVOKE_URL + "/payment/consul", String.class);

        return forObject;

    }

}

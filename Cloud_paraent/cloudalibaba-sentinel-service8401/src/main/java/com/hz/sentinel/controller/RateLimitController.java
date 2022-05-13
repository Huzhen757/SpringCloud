package com.hz.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.sentinel.config.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
    // 测试按资源名限流
    @GetMapping("/sentinel/byresource")
    @SentinelResource(value = "resource", blockHandler = "handException")
    public CommonResult getResource(){
        return new CommonResult(200, "按照资源名测试 success!", new Payment(2022L, "sentinelResource_1"));
    }
    // 自定义的限流处理方法
    public CommonResult handException(BlockException exception){
        return new CommonResult(500, exception.getClass().getCanonicalName() + "\t service is not available");
    }

    // 按照URL限流
    @GetMapping("/sentinel/byurl")
    @SentinelResource(value = "byurl")
    public CommonResult getURL(){
        return new CommonResult(200, "按照URL限流测试 success!", new Payment(2022L, "SentinelResource_byURL"));
    }

    // 按照自定义违反规则的处理方法
    @GetMapping("/sentinel/customerHandler")
    @SentinelResource(value = "customerHandler", blockHandlerClass = CustomerBlockHandler.class,
    blockHandler = "handlerException2")
    public CommonResult customerHandler(){
        return new CommonResult(200, "自定义customerBlockHandler处理 success!", new Payment(2022L, "SentinelResource_customHandler"));
    }
}

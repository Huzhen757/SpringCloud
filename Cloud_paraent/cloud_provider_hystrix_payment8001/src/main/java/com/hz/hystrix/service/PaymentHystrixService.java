package com.hz.hystrix.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentHystrixService {
    // ============服务降级fallback
    public String paymentInfo_success(Integer id){
        return "Thread pool: " + Thread.currentThread().getName() + " paymentInfo_id: " + id + " success";
    }

    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "5000")})
    public String paymentInfo_timeout(Integer id){
       // int i = 10 / 0;  故意运行出错
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Thread pool: " + Thread.currentThread().getName() + " paymentInfo_id: " + id + " time out";
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "Thread pool: " + Thread.currentThread().getName()+"\t 系统繁忙或者运行报错, id: " + id+" ErrorHandler";
    }

    // =============服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled", value = "true"), // 是否开启熔断器
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value = "60"), // 失败率达到多少后熔断
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id < 0){
            throw new RuntimeException("*****id 不能负载");
        }
        String serialNum = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t 调用成功 流水号：" + serialNum;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id: "+id+ "不能负载，稍后再试";
    }
}

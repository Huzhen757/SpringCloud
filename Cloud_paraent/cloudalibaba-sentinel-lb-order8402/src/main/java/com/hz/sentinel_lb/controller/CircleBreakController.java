package com.hz.sentinel_lb.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.sentinel_lb.service.SentinelPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
public class CircleBreakController {

    public final String URL = "http://cloudalibaba-sentinel-lb-provider";
    @Value("${service-url.nacos-user-service}")
    private String SERVICE_URL;

    @Autowired
    private RestTemplate restTemplate;
    // ########### 只配置fallback  -> 负责运行时异常的处理
    @GetMapping("/sentinel/consumer/payment/fallback/{id}")
   // @SentinelResource(value = "fallback") 未指定对应的fallback方法 运行时异常直接在前台输出
    @SentinelResource(value = "fallback", fallback = "handlerFallback")
    public CommonResult<Payment> fallback(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }
    // 自定义fallback方法: 返回值类型与原函数一致 参数列表一致(可以额外增加一个Throwable类型的参数 用来接受异常)
    public CommonResult handlerFallback(@PathVariable("id") Long id, Throwable e){
        Payment payment = new Payment(id, "no data");
        return new CommonResult(500, "fallback异常处理: " + e.getMessage(), payment);
    }

    // ########### 只配置blockHandler -> 负责违规配置的处理
    @GetMapping("/sentinel/consumer/payment/blockhandler/{id}")
    @SentinelResource(value = "blockHandler", blockHandler = "myBlockHandler",
    exceptionsToIgnore = {IllegalArgumentException.class}) // 出现指定的异常直接忽略, 没有服务降级
    public CommonResult<Payment> testBlockHandler(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }

    public CommonResult myBlockHandler(@PathVariable("id") Long id, BlockException exception){
        Payment payment = new Payment(id, "no data");
        return new CommonResult(500, "block handler异常处理: "+exception.getMessage(), payment);
    }

    // ############ fallback和blockHandler都配置
    @GetMapping("/sentinel/consumer/payment/all/{id}")
    @SentinelResource(value = "fallbackAndBlockHandler", blockHandler = "myBlockHandler", fallback = "handlerFallback")
    public CommonResult<Payment> testBlockHandlerAndFallback(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }

    // ############# 服务熔断/降级 OpenFeign中的实现
    @Resource
    private SentinelPaymentService service;

    @GetMapping(value = "/openfeign/consumer/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id){
        return service.getById(id);
    }

}

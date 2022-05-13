package com.hz.openfeign.controller;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.openfeign.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService service;

    @Value("${server.port}")
    private String serverPort;

    // 服务发现来获取注册到server的服务信息
    @Autowired
    private DiscoveryClient discoveryClient;

    // 使用RequestBody注解表示使用json数据进行传输
    @PostMapping("/payment/save")
    public CommonResult save(@RequestBody Payment payment){
        int res = service.save(payment);
        log.info("插入记录数：" + res);
        if(res > 0){
            return new CommonResult(200, "插入数据成功,serverport: " + serverPort, res);
        }else{
            return new CommonResult(444, "插入数据失败", null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment res = service.getPaymentById(id);
        log.info("根据id查询结果：" + res);
        if(res != null){
            return new CommonResult(200, "查询成功,serverport: " + serverPort, res);
        }else{
            return new CommonResult(555, "记录id不存在", null);
        }
    }

    @GetMapping("/payment/selectall")
    public CommonResult getAllPayment(){
        List<Payment> res = service.selectAll();
        log.info("所有记录：" + res);
        if(res != null){
            return new CommonResult(200, "查询成功,serverPort: " + serverPort, res);
        }else{
            return new CommonResult(444, "查询失败", null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object getDiscovery(){
        // 获取所有的微服务(payment,order,...)
        List<String> services = discoveryClient.getServices();
        for(String service : services){
            log.info("******service: " + service);
        }
        // 获取某一个微服务的所有实例(payment8001,payment8002,...)
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for(ServiceInstance instance : instances){
            log.info("******instance:\t" + instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient.toString();
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        return "当前提供的微服务实例端口：" + serverPort;
    }

    // 8001端口主动暂停程序
    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "当前端口：" + serverPort;
    }

    // Sleuth链路监控测试
    @GetMapping("/payment/sleuth")
    public String getZipkin(){
        return "this is Payment8001 service base on Eureka, trying use sleuth to monitor...";
    }


}

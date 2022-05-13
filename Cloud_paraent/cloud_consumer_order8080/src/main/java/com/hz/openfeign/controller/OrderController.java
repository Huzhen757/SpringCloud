package com.hz.openfeign.controller;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.loadBalancer.MyLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {
    // 对于单机版的服务订单地址可以写死
    // public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private MyLoadBalancer loaderHandler;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/consumer/payment/save")
    public CommonResult<Payment> save(Payment payment){

        return restTemplate.postForObject(PAYMENT_URL + "/payment/save", payment, CommonResult.class);
        // postForEntity
        // return restTemplate.postForEntity(PAYMENT_URL + "/payment/save", payment, CommonResult.class).getBody();
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/"+id, CommonResult.class);

    }

    @GetMapping("/consumer/paymentForEntity/get/{id}")
    public CommonResult<Payment> getPaymentById2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if(forEntity.getStatusCode().is2xxSuccessful()){
            log.info("请求的状态码：" + forEntity.getStatusCode() + "\t" + forEntity.getHeaders());
            return forEntity.getBody();
        }else {
            return new CommonResult<>(444, "options failed!");
        }

    }

    @GetMapping("/consumer/payment/payment/getAll")
    public CommonResult<Payment> getAllPayment(){
        return restTemplate.getForObject(PAYMENT_URL + "/payment/selectall", CommonResult.class);
    }

    @GetMapping("/consumer/payment/lb")
    public String getPaymentMyLB(){
        // 获取该微服务下的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances == null || instances.size() <= 0){
            return null;
        }
        // 根据微服务实例列表通过自定义的轮询得到当前需要访问的实例是哪一个
        ServiceInstance service = loaderHandler.getInstance(instances);
        URI uri = service.getUri();
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
    }

    // Sleuth链路监控测试
    @GetMapping("/consumer/payment/sleuth")
    public String getZipkin(){
        String res = restTemplate.getForObject("http://localhost:8001" + "/payment/sleuth", String.class);
        return res + "\t this order80 service base on Eureka...";
    }
}

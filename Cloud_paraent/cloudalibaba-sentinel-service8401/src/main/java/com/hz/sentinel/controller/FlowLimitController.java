package com.hz.sentinel.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/sentinel/test1")
    public String test1(){
        log.info(Thread.currentThread().getName() + "\t process...");
        return "****Sentinel test1";
    }

    @GetMapping("/sentinel/test2")
    public String test2(){
        return "****Sentinel test2";
    }

    // 流控模式-关联
    @GetMapping("/sentinel/test3")
    public String test3(){
        return "****Sentinel test3 关联 test2";
    }

    // 降级规则-RT
    @GetMapping("/sentinel/test4")
    public String test4(){
//        try {
//            TimeUnit.SECONDS.sleep(1);
//            log.info("*****sentinel 降级规则-RT test");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("*****sentinel 降级规则-异常比例 test");
//        int i=1/0;
        return "****Sentinel test4...";
    }
    // 如果出现fallback，执行指定的blockHandler方法
    @GetMapping("/sentinel/hotkey")
    @SentinelResource(value = "hotkey", blockHandler = "deal_testHotkey")
    public String testHotKey(@RequestParam(value = "username",required = false)String name,
                             @RequestParam(value = "age",required = false)String age){

        return "****Sentinel 热点规则";
    }

    public String deal_testHotkey(String p1, String p2, BlockException exception){

        return "****Sentinel my fallback method deal_testHotkey exec...☹";
    }

}

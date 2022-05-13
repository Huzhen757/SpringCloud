package com.hz.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController // 开启Nacos的动态刷新功能(广播通知 -> 配置自动更新)
public class ConfigCenterController {

//    @Value("${config.info}")
//    private String configInfo;

    @GetMapping("/nacos/config/info")
    public String getConfigInfo(){

        return "nacos config center get config info from NacosConfigCenter: ";
    }
}

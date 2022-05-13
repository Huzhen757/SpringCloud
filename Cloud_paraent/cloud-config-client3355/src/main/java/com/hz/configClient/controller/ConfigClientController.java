package com.hz.configClient.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 开启刷新功能
public class ConfigClientController {
    @Value("${server.port}")
    private String serverPort;

    // 测试是否能从客户端3355获取到 3344在github上得到的配置信息config-dev.yaml
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo(){
        return "server port: " + serverPort + "\t configInfo: " + configInfo;
    }
}

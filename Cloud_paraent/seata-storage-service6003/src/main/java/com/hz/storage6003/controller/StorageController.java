package com.hz.storage6003.controller;

import com.hz.entities.CommonResult;
import com.hz.storage6003.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class StorageController {

    @Autowired
    private StorageService service;

    @RequestMapping("/storage/decrease")
    public CommonResult decrease(Long productId, Integer count){
        service.decrease(productId, count);
        return new CommonResult(200, "seata-storage-service decrease storage complete!");
    }
}

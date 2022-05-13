package com.hz.service;

import com.hz.entities.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用库存微服务
 */
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    // 库存扣减
    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId")Long productId, @RequestParam("count")Integer count);


}

package com.hz.sentinel.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hz.entities.CommonResult;

// 自定义服务降级的处理逻辑类
public class CustomerBlockHandler {

    public static CommonResult handlerException1(BlockException exception){

        return new CommonResult(200, "用户自定义 global handler exception==============1");
    }

    public static CommonResult handlerException2(BlockException exception){

        return new CommonResult(200, "用户自定义 global handler exception===============2");
    }
}

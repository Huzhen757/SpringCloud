package com.hz.sentinel_lb.service.impl;

import com.hz.entities.CommonResult;
import com.hz.entities.Payment;
import com.hz.sentinel_lb.service.SentinelPaymentService;
import org.springframework.stereotype.Component;

/**
 * order8402调用9003和9004, 如果9003宕机, 继续访问则会执行@FeignClient注解中fallback指明的类中的方法
 * 而该类是OpenFeign接口的实现类, 所以不会在前台输出错误页面而是执行实现类中的fallback方法
 */
@Component
public class SentinelPaymentServiceImpl implements SentinelPaymentService {
    @Override
    public CommonResult<Payment> getById(Long id) {
        return new CommonResult<>(500, "服务降级, OpenFeign中的fallback方法", new Payment(id, "openFeign for fallback"));
    }
}

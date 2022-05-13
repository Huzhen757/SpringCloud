package com.hz.service.impl;

import com.hz.dao.OrderDao;
import com.hz.domain.Order;
import com.hz.service.AccountService;
import com.hz.service.OrderService;
import com.hz.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Resource
    private AccountService accountService;

    @Autowired
    private StorageService storageService;

    @Override
    // 使用全局事务注解，配置唯一的name，rollbackFor指定发生什么样的异常进行回滚
   // @GlobalTransactional(name = "order-global-tx", rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("**********begin create order...");
        orderDao.create(order);

        log.info("********* 订单微服务调用库存微服务 根据商品id和卖出的数量 进行库存扣减");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("********* 订单微服务调用账户微服务 根据用户id和买商品的金额 进行账户余额扣减");
        accountService.decrease(order.getUserId(), order.getMoney());

        log.info("********* 修改订单状态 从0->1");
        orderDao.update(order.getUserId(), 0);
        log.info("**********end create order...");
    }

    @Override
    public void update(Long userId, Integer status) {
        orderDao.update(userId, status);
    }

}

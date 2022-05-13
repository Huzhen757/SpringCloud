package com.hz.service;

import com.hz.domain.Order;


public interface OrderService {

    void create(Order order);

    void update(Long userId, Integer status);

}

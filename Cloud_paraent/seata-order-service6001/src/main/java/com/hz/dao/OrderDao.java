package com.hz.dao;

import com.hz.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface OrderDao {
    // 新建订单
    public void create(Order order);

    // 修改订单信息, status:0 -> 1
    void update(@Param("userId")Long userId, @Param("status")Integer status);


}

package com.hz.dao;

import com.hz.entities.Payment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PaymentDao {

    List<Payment> selectAll();

    int save(Payment payment);

    Payment getPaymentById(@Param("id") Long id);

}

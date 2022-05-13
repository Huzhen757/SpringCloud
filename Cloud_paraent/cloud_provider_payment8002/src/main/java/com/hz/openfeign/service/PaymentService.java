package com.hz.openfeign.service;

import com.hz.entities.Payment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentService {

    List<Payment> selectAll();

    int save(Payment payment);

    Payment getPaymentById(@Param("id") Long id);
}

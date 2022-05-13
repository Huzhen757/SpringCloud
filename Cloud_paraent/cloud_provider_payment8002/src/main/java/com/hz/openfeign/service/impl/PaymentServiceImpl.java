package com.hz.openfeign.service.impl;

import com.hz.dao.PaymentDao;
import com.hz.entities.Payment;
import com.hz.openfeign.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentDao dao;

    @Override
    public List<Payment> selectAll() {
        List<Payment> res = dao.selectAll();
        return res;
    }

    @Override
    public int save(Payment payment) {
        return dao.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return dao.getPaymentById(id);
    }
}

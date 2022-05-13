package com.hz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountDao {
    // 扣减对应用户的余额
    void decrease(@Param("userId")Long userId, @Param("money") BigDecimal money);
}

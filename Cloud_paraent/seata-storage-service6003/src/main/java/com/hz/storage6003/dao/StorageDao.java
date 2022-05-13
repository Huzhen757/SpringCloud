package com.hz.storage6003.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StorageDao {
    // 根据商品id和用户购买的数量 从库存中减去对应的值
    void decrease(@Param("productId")Long productId, @Param("count")Integer count);
}

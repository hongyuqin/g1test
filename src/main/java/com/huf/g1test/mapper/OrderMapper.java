package com.huf.g1test.mapper;

import com.huf.g1test.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    
    Order selectByOrderNo(@Param("orderNo") String orderNo);
    
    int insert(Order order);
    
    int updateStatus(@Param("orderNo") String orderNo, @Param("status") String status);
} 
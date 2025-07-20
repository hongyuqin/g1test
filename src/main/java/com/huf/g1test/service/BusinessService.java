package com.huf.g1test.service;

import com.huf.g1test.entity.Order;

import java.math.BigDecimal;

public interface BusinessService {
    
    /**
     * 创建订单并扣减账户余额
     * 演示分布式事务
     */
    boolean createOrderAndDeductBalance(String userId, String orderNo, BigDecimal amount);
    
    /**
     * 模拟异常情况，测试事务回滚
     */
    boolean createOrderWithException(String userId, String orderNo, BigDecimal amount);
} 
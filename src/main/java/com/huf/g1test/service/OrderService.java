package com.huf.g1test.service;

import com.huf.g1test.entity.Order;

public interface OrderService {
    
    Order getOrder(String orderNo);
    
    boolean createOrder(Order order);
    
    boolean updateOrderStatus(String orderNo, String status);
} 
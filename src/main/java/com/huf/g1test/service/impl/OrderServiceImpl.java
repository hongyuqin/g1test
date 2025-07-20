package com.huf.g1test.service.impl;

import com.huf.g1test.entity.Order;
import com.huf.g1test.mapper.OrderMapper;
import com.huf.g1test.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order getOrder(String orderNo) {
        log.info("查询订单信息，orderNo: {}", orderNo);
        return orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public boolean createOrder(Order order) {
        log.info("创建订单，order: {}", order);
        int result = orderMapper.insert(order);
        return result > 0;
    }

    @Override
    public boolean updateOrderStatus(String orderNo, String status) {
        log.info("更新订单状态，orderNo: {}, status: {}", orderNo, status);
        int result = orderMapper.updateStatus(orderNo, status);
        return result > 0;
    }
} 
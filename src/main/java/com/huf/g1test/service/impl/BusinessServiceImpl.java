package com.huf.g1test.service.impl;

import com.huf.g1test.entity.Account;
import com.huf.g1test.entity.Order;
import com.huf.g1test.service.AccountService;
import com.huf.g1test.service.BusinessService;
import com.huf.g1test.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Override
    @GlobalTransactional(name = "create-order-deduct-balance", rollbackFor = Exception.class)
    public boolean createOrderAndDeductBalance(String userId, String orderNo, BigDecimal amount) {
        log.info("开始创建订单并扣减余额，userId: {}, orderNo: {}, amount: {}", userId, orderNo, amount);
        
        // 1. 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAmount(amount);
        order.setStatus("CREATED");
        
        boolean orderResult = orderService.createOrder(order);
        if (!orderResult) {
            log.error("创建订单失败");
            throw new RuntimeException("创建订单失败");
        }
        log.info("订单创建成功，orderNo: {}", orderNo);
        
        // 2. 扣减账户余额
        BigDecimal deductAmount = amount.negate(); // 负数表示扣减
        boolean accountResult = accountService.updateBalance(userId, deductAmount);
        if (!accountResult) {
            log.error("扣减账户余额失败");
            throw new RuntimeException("扣减账户余额失败");
        }
        log.info("账户余额扣减成功，userId: {}, amount: {}", userId, deductAmount);
        
        // 3. 更新订单状态
        boolean updateResult = orderService.updateOrderStatus(orderNo, "PAID");
        if (!updateResult) {
            log.error("更新订单状态失败");
            throw new RuntimeException("更新订单状态失败");
        }
        log.info("订单状态更新成功，orderNo: {}, status: PAID", orderNo);
        
        log.info("分布式事务执行成功");
        return true;
    }

    @Override
    @GlobalTransactional(name = "create-order-with-exception", rollbackFor = Exception.class)
    public boolean createOrderWithException(String userId, String orderNo, BigDecimal amount) {
        log.info("开始创建订单（模拟异常），userId: {}, orderNo: {}, amount: {}", userId, orderNo, amount);
        
        // 1. 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAmount(amount);
        order.setStatus("CREATED");
        
        boolean orderResult = orderService.createOrder(order);
        if (!orderResult) {
            log.error("创建订单失败");
            throw new RuntimeException("创建订单失败");
        }
        log.info("订单创建成功，orderNo: {}", orderNo);
        
        // 2. 扣减账户余额
        BigDecimal deductAmount = amount.negate();
        boolean accountResult = accountService.updateBalance(userId, deductAmount);
        if (!accountResult) {
            log.error("扣减账户余额失败");
            throw new RuntimeException("扣减账户余额失败");
        }
        log.info("账户余额扣减成功，userId: {}, amount: {}", userId, deductAmount);
        
        // 3. 模拟异常，测试事务回滚
        log.info("模拟异常，触发事务回滚");
        throw new RuntimeException("模拟业务异常，测试事务回滚");
    }
} 
package com.huf.g1test.controller;

import com.huf.g1test.entity.Account;
import com.huf.g1test.service.AccountService;
import com.huf.g1test.service.BusinessService;
import com.huf.g1test.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/seata")
@Slf4j
public class SeataDemoController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    /**
     * 初始化账户
     */
    @PostMapping("/init-account")
    public Map<String, Object> initAccount(@RequestParam String userId, @RequestParam BigDecimal balance) {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = new Account();
            account.setUserId(userId);
            account.setBalance(balance);
            account.setCurrency("CNY");
            account.setStatus("ACTIVE");
            
            boolean success = accountService.createAccount(account);
            if (success) {
                result.put("success", true);
                result.put("message", "账户初始化成功");
                result.put("data", account);
            } else {
                result.put("success", false);
                result.put("message", "账户初始化失败");
            }
        } catch (Exception e) {
            log.error("初始化账户异常", e);
            result.put("success", false);
            result.put("message", "初始化账户异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 查询账户信息
     */
    @GetMapping("/account/{userId}")
    public Map<String, Object> getAccount(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = accountService.getAccount(userId);
            if (account != null) {
                result.put("success", true);
                result.put("data", account);
            } else {
                result.put("success", false);
                result.put("message", "账户不存在");
            }
        } catch (Exception e) {
            log.error("查询账户异常", e);
            result.put("success", false);
            result.put("message", "查询账户异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 创建订单并扣减余额（正常流程）
     */
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam String userId, @RequestParam BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderNo = "ORDER_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            
            boolean success = businessService.createOrderAndDeductBalance(userId, orderNo, amount);
            if (success) {
                result.put("success", true);
                result.put("message", "订单创建成功");
                result.put("orderNo", orderNo);
            } else {
                result.put("success", false);
                result.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            log.error("创建订单异常", e);
            result.put("success", false);
            result.put("message", "创建订单异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 创建订单并扣减余额（模拟异常）
     */
    @PostMapping("/create-order-exception")
    public Map<String, Object> createOrderWithException(@RequestParam String userId, @RequestParam BigDecimal amount) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderNo = "ORDER_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            
            businessService.createOrderWithException(userId, orderNo, amount);
            
            result.put("success", true);
            result.put("message", "订单创建成功");
            result.put("orderNo", orderNo);
        } catch (Exception e) {
            log.error("创建订单异常（预期异常）", e);
            result.put("success", false);
            result.put("message", "创建订单异常（预期异常）: " + e.getMessage());
            result.put("expected", true);
        }
        return result;
    }

    /**
     * 查询订单信息
     */
    @GetMapping("/order/{orderNo}")
    public Map<String, Object> getOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            com.huf.g1test.entity.Order order = orderService.getOrder(orderNo);
            if (order != null) {
                result.put("success", true);
                result.put("data", order);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            log.error("查询订单异常", e);
            result.put("success", false);
            result.put("message", "查询订单异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "Seata Demo Service is running");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
} 
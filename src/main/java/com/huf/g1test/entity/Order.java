package com.huf.g1test.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private String userId;
    private BigDecimal amount;
    private String status;
    private String createTime;
    private String updateTime;
} 
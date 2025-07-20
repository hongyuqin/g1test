package com.huf.g1test.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Long id;
    private String userId;
    private BigDecimal balance;
    private String currency;
    private String status;
    private String createTime;
    private String updateTime;
} 
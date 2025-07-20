package com.huf.g1test.service;

import com.huf.g1test.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    
    Account getAccount(String userId);
    
    boolean updateBalance(String userId, BigDecimal amount);
    
    boolean createAccount(Account account);
} 
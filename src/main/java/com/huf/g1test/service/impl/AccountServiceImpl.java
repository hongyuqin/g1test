package com.huf.g1test.service.impl;

import com.huf.g1test.entity.Account;
import com.huf.g1test.mapper.AccountMapper;
import com.huf.g1test.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account getAccount(String userId) {
        log.info("查询账户信息，userId: {}", userId);
        return accountMapper.selectByUserId(userId);
    }

    @Override
    public boolean updateBalance(String userId, BigDecimal amount) {
        log.info("更新账户余额，userId: {}, amount: {}", userId, amount);
        int result = accountMapper.updateBalance(userId, amount);
        return result > 0;
    }

    @Override
    public boolean createAccount(Account account) {
        log.info("创建账户，account: {}", account);
        int result = accountMapper.insert(account);
        return result > 0;
    }
} 
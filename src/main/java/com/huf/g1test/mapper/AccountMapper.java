package com.huf.g1test.mapper;

import com.huf.g1test.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {
    
    Account selectByUserId(@Param("userId") String userId);
    
    int updateBalance(@Param("userId") String userId, @Param("amount") BigDecimal amount);
    
    int insert(Account account);
} 
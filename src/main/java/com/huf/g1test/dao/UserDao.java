package com.huf.g1test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huf.g1test.pojo.User;
import org.springframework.stereotype.Repository;

@Repository // 表示持久层
public interface UserDao extends BaseMapper<User> {
    
}
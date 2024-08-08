package com.huf.g1test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huf.g1test.entity.User;
import org.springframework.stereotype.Repository;

@Repository // 表示持久层
public interface UserMapper extends BaseMapper<User> {
    
}
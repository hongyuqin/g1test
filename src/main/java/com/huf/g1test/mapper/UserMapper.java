package com.huf.g1test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huf.g1test.entity.User;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 
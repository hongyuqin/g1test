package com.huf.g1test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huf.g1test.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    
    /**
     * 从主库查询用户列表（业务接口）
     */
    List<User> getUsersFromMaster();
    
    /**
     * 从从库查询用户列表（管理后台接口）
     */
    List<User> getUsersFromSlave();
    
    /**
     * 添加用户（写操作，使用主库）
     */
    boolean addUser(User user);
} 
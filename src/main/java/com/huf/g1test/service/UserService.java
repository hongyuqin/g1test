package com.huf.g1test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huf.g1test.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    
    /**
     * 查询用户列表（Sharding-JDBC自动路由到主库）
     */
    List<User> getUsersFromMaster();
    
    /**
     * 查询用户列表（Sharding-JDBC自动路由到从库）
     */
    List<User> getUsersFromSlave();
    
    /**
     * 添加用户（写操作，Sharding-JDBC自动路由到主库）
     */
    boolean addUser(User user);
    
    /**
     * 事务中添加用户（Sharding-JDBC自动路由到主库）
     */
    boolean addUserWithTransaction(User user);
    
    /**
     * 默认事务中添加用户（Sharding-JDBC自动路由到主库）
     */
    boolean addUserDefaultTransaction(User user);
} 
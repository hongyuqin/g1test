package com.huf.g1test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huf.g1test.entity.User;
import com.huf.g1test.mapper.UserMapper;
import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> getUsersFromMaster() {
        log.info("【业务查询】开始执行查询，预期路由到主库...");
        List<User> users = this.list(new QueryWrapper<>());
        log.info("【业务查询】查询完成，获取到 {} 条记录", users.size());
        return users;
    }

    @Override
    public List<User> getUsersFromSlave() {
        log.info("【管理查询】开始执行查询，预期路由到从库...");
        List<User> users = this.list(new QueryWrapper<>());
        log.info("【管理查询】查询完成，获取到 {} 条记录", users.size());
        return users;
    }

    @Override
    public boolean addUser(User user) {
        log.info("【写操作】开始执行插入，预期路由到主库...");
        boolean result = this.save(user);
        log.info("【写操作】插入完成，结果: {}", result ? "成功" : "失败");
        return result;
    }
    
    /**
     * 事务方法示例 - ShardingSphere会自动路由到主库
     */
    @Transactional
    public boolean addUserWithTransaction(User user) {
        log.info("【事务操作】开始执行事务，预期路由到主库...");
        boolean result = this.save(user);
        log.info("【事务操作】事务完成，结果: {}", result ? "成功" : "失败");
        return result;
    }
    
    /**
     * 默认事务方法示例 - ShardingSphere会自动路由到主库
     */
    @Transactional
    public boolean addUserDefaultTransaction(User user) {
        log.info("【默认事务】开始执行默认事务，预期路由到主库...");
        boolean result = this.save(user);
        log.info("【默认事务】默认事务完成，结果: {}", result ? "成功" : "失败");
        return result;
    }
} 
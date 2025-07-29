package com.huf.g1test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huf.g1test.annotation.DataSource;
import com.huf.g1test.config.DataSourceEnum;
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
    @DataSource(DataSourceEnum.MASTER)
    public List<User> getUsersFromMaster() {
        log.info("从主库查询用户列表");
        return this.list(new QueryWrapper<>());
    }

    @Override
    @DataSource(DataSourceEnum.SLAVE)
    public List<User> getUsersFromSlave() {
        log.info("从从库查询用户列表");
        return this.list(new QueryWrapper<>());
    }

    @Override
    @DataSource(DataSourceEnum.MASTER)
    public boolean addUser(User user) {
        log.info("向主库添加用户: {}", user.getName());
        return this.save(user);
    }
    
    /**
     * 事务方法示例 - 强制使用主库
     */
    @Transactional
    @DataSource(DataSourceEnum.MASTER)  // 事务中明确指定主库
    public boolean addUserWithTransaction(User user) {
        log.info("事务中添加用户: {}", user.getName());
        // 事务中的所有操作都在主库执行
        boolean result = this.save(user);
        // 可以执行其他需要事务的操作
        return result;
    }
    
    /**
     * 事务方法示例 - 不指定数据源，默认主库
     */
    @Transactional
    public boolean addUserDefaultTransaction(User user) {
        log.info("默认事务中添加用户: {}", user.getName());
        // 默认使用主库，保证事务一致性
        return this.save(user);
    }
} 
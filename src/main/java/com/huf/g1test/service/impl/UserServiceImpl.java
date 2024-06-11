package com.huf.g1test.service.impl;

import com.huf.g1test.dao.UserDao;
import com.huf.g1test.pojo.User;
import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${name:hongyuqin}")
    private String name;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private KafkaTemplate<Object, Object> template;

    private AtomicInteger counter = new AtomicInteger(0);

    public void printName(){
        log.info("name is : {}", name);
    }

    /**
     * 测试事务
     */
    @Override
    public void addUser1(){
        userDao.insert(User.builder().name("hongmin").age(27).email("user1@qq.com").build());
        userService.addUser2();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser2(){
        userDao.insert(User.builder().name("honghongxin").age(26).email("user22@qq.com").build());
        //template.send("topic_input", "sorry");
        if (counter.getAndAdd(1) == 0) {
            log.info("throw first exception");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("xx");
        }
    }

    @Override
    public void multiAddUser(){
        for (int i = 0; i < 10; i++) {
            try {
                userService.addUser2();
            } catch (Exception e) {
                log.error("exception occur:",e);
            }
        }
    }

}

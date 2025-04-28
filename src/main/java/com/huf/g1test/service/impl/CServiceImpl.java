package com.huf.g1test.service.impl;

import com.huf.g1test.dao.UserDao;
import com.huf.g1test.pojo.User;
import com.huf.g1test.rocketmq.ProducerDemo;
import com.huf.g1test.service.CService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CServiceImpl implements CService {
    @Autowired
    private ProducerDemo producerDemo;
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional
    public void testTransactional() {
        log.info("test");
        for(int i = 0 ;i<1000;i++){
            producerDemo.sendMessage("TestTopic","hello"+i);
            //            producerDemo.sendOrderMessage("hello"+i);
        }
        //producerDemo.sendAfterCommit("TestTopic","hello");

        User user = new User();
        user.setName("hongyuqin");
        user.setAge(25);
        userDao.insert(user);
        //throw new RuntimeException();
    }
}

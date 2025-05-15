package com.huf.g1test.service.impl;

import com.huf.g1test.dao.UserDao;
import com.huf.g1test.pojo.User;
import com.huf.g1test.service.CService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CServiceImpl implements CService {
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional
    public void testTransactional() {
        log.info("test");

        User user = new User();
        user.setName("hongyuqin");
        user.setAge(25);
        userDao.insert(user);
        //throw new RuntimeException();
    }
}

package com.huf.g1test.service;

import org.springframework.transaction.annotation.Transactional;


public interface UserService {

    public void addUser1();

    @Transactional
    public void addUser2();

    public void multiAddUser();

}

package com.huf.g1test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AService {
    @Autowired

    private BService bService;

    public void test(){
        System.out.println(bService);
    }
}
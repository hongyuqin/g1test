package com.huf.g1test.controller;

import com.huf.g1test.service.CService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {
    @Autowired
    private CService cService;
    @Autowired
    private Redisson redisson;

    @GetMapping("test")
    public void testTransactional(){
        cService.testTransactional();
    }

    @GetMapping("testLock")
    public void testLock() throws InterruptedException {
        RLock lock = redisson.getLock("lock");
        //看看怎么限流
        RRateLimiter rateLimiter = redisson.getRateLimiter("rate:limit");
        rateLimiter.acquire();
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);

        lock.unlock();

    }
}

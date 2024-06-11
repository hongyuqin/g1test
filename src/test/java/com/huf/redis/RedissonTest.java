package com.huf.redis;

import com.huf.g1test.G1testApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
/*import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = G1testApplication.class)
@Slf4j
public class RedissonTest {
    /*@Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() {
        RLock rLock = redissonClient.getLock("lock3");
        try {
            rLock.lock();
        }finally {
            rLock.unlock();
        }
    }*/

    @Test
    public void test1(){

    }
}

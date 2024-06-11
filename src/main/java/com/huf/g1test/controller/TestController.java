package com.huf.g1test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.huf.g1test.annotation.MyValidation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("test")
public class TestController {
    static class OOMObject {

    }

    static class SynAddRunnable implements Runnable {
        int a, b;

        public SynAddRunnable(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }
    }

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("test/{name}")
    public String test(@NotNull @PathVariable("name") String name) {
        Jedis jedis = jedisPool.getResource();
        String val = jedis.get("name");
        log.info("val is : {}", val);
        jedis.close();
        return "test";
    }

    @RequestMapping("oom")
    public void testOOM() {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }

    @RequestMapping("stackflow")
    public void testStackFlow() {
        for (int i = 0; i < 100; i++) {
            new Thread(new SynAddRunnable(1, 2)).start();
            new Thread(new SynAddRunnable(2, 1)).start();
        }
    }

    @GetMapping(value = "/hello")
    @SentinelResource(value="hello",blockHandler = "handleBlock")
    public String hello() {
        return "Hello Sentinel";
    }

    private String handleBlock(BlockException exception) {
        return "Opps,blocked!";
    }

    @GetMapping(value = "/mye")
    @SentinelResource("mye")
    public String mye() {
        if (true) {
            throw new RuntimeException("mye");
        }
        return "mye Sentinel";
    }

    @GetMapping(value = "/myrate")
    @SentinelResource("myrate")
    public String myrate() {
        return "myrate Sentinel";
    }

    @Autowired
    private RedissonClient redissonClient;
    @GetMapping("/test-redisson-lock")
    public void testRedissonLock() throws InterruptedException {
        log.info("testRedissonLock ... ");
        RLock lock = redissonClient.getLock("myLock");
        lock.lock();
        TimeUnit.SECONDS.sleep(100);
        lock.unlock();
    }

}

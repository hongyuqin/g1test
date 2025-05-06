package com.huf.g1test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
   /* @Autowired
    private Redisson redisson;
*/
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


   /* @GetMapping("testLock")
    public void testLock() throws InterruptedException {
        RLock lock = redisson.getLock("lock");
        //看看怎么限流
        RRateLimiter rateLimiter = redisson.getRateLimiter("rate:limit");
        rateLimiter.acquire();
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);

        lock.unlock();
    }*/

    @GetMapping("testCluster")
    public void testCluster(){
        redisTemplate.opsForValue().set("test_key", "Hello Redis Cluster!");
        String value = (String) redisTemplate.opsForValue().get("test_key");
        System.out.println("Value: " + value); // 输出 "Hello Redis Cluster!"
    }
}

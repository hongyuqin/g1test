package com.huf.redis;

import com.huf.g1test.G1testApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

@SpringBootTest(classes = G1testApplication.class)
@Slf4j
public class LuaTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> redisScript;

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testGet(){
        log.info("name is :{}",redisTemplate.opsForValue().get("name"));
    }
    @Test
    public void test(){
        List<String> keys = Arrays.asList("lock");
        Object execute = redisTemplate.execute(redisScript, keys, "1234");
        log.info("result is :{}", execute);
    }
    //看下hscan是怎么执行
    @Test
    public void testHScan(){
        Cursor<Map.Entry<String, String>> cursor = redisTemplate.opsForHash().scan("oas_fendan3",ScanOptions.scanOptions().match("*").count(2).build());

        while (cursor.hasNext()){
            Map.Entry<String,String> map = cursor.next();
            log.info("k - v : {} {}", map.getKey(),map.getValue());
            //break;
        }
        cursor.close();
    }
    @Test
    public void testJedis(){
        Jedis jedis = jedisPool.getResource();
        String val = jedis.get("name");
        log.info("val is : {}", val);
        jedis.close();
    }

}

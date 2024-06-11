package com.huf.util;

import com.huf.g1test.G1testApplication;
import com.huf.g1test.rocketmq.ProducerDemo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
@SpringBootTest(classes = G1testApplication.class)
public class MqTest {
    @Autowired
    private ProducerDemo producerDemo;

    @Test
    public void testRocketMq() throws Exception{
        producerDemo.sendMessage("consumer_topic","hahahahah");
        TimeUnit.SECONDS.sleep(100);
    }
}

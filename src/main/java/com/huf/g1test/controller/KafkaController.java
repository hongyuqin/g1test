package com.huf.g1test.controller;

import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("kafka")
public class KafkaController {
    @Autowired
    private KafkaTemplate<Object, Object> template;
    @Autowired
    private UserService userService;

    @RequestMapping("send")
    @Transactional
    public String send() throws Exception{
        log.info("sending ...");

        String topic = "topic_consume_for";
        String value = "test";
        for(int i = 0 ;i<100;i++) {
            // 发送相同的消息多次
            template.send(new ProducerRecord<>(topic, value+i));

        }

        //int j = 1/0;
        return "test";
    }


}

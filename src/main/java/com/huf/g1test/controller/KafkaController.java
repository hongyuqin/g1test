package com.huf.g1test.controller;

import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    public String send() throws Exception{
        log.info("sending ...");
        String topic = "topic_consume_for";
        String value = "test";
        for(int i = 0 ;i<100;i++) {
            // 发送相同的消息多次
            template.send(new ProducerRecord<>(topic, value+i));
        }
        return "test";
    }

    @RequestMapping("group_send")
    public String groupSend() throws Exception{
        log.info("groupSending ...");
        String value = "group_test_";
        template.send(new ProducerRecord<>("topic_replica_for", value+Instant.now().getEpochSecond()));
        return "test";
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic("topic_replica_for", 1, (short) 3);
    }


    @Bean
    public NewTopic myTopic1() {
        return new NewTopic("topic_replica1_for", 2, (short) 3);
    }
}

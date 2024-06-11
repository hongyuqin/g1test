package com.huf.g1test.controller;

import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String send() {
        log.info("sending ...");

        String topic = "topic_input2";
        String key = "my-key";
        String value = "my-value";

        // 发送相同的消息多次
        for (int i = 0; i < 10; i++) {
            template.send(new ProducerRecord<>(topic, key, value+i));
        }
        return "test";
    }

    @RequestMapping("send_trans")
    public String send_trans() {
        userService.addUser1();
        return "test";
    }

}

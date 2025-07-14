package com.huf.g1test.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("kafka-test")
public class KafkaTestController {
    @Autowired
    private KafkaTemplate<Object, Object> template;

    /**
     * 发送一批消息到Kafka topic。可指定第几条消息内容为"fail"，用于测试消费端异常处理。
     * @param count 发送消息总数
     * @param failIndex 第几条消息内容为"fail"（从0开始），-1表示不插入fail
     */
    @RequestMapping("sendBatch")
    public String sendBatch(@RequestParam(defaultValue = "10") int count,
                            @RequestParam(defaultValue = "-1") int failIndex) {
        log.info("sending batch ... count={}, failIndex={}", count, failIndex);
        String topic = "topic_test";
        for(int i = 0; i < count; i++) {
            String value = (i == failIndex) ? ("fail-" + i) : ("msg-" + i);
            template.send(new ProducerRecord<>(topic, value));
        }
        return "sent " + count + " messages to " + topic + (failIndex >= 0 ? (", fail at " + failIndex) : "");
    }
}

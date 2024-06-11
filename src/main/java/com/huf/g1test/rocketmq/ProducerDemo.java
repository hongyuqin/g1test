package com.huf.g1test.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class ProducerDemo {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送消息的实例
    public void sendMessage(String topic, String msg) {
        Message message = MessageBuilder.withPayload(msg.getBytes()).build();

//        message.setDelayTimeSec(100);
        rocketMQTemplate.syncSendDelayTimeSeconds(topic, message,10);
        log.info("sendMessage : {}", msg);
    }
}

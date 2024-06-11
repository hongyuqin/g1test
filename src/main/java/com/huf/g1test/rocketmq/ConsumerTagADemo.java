package com.huf.g1test.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

//@RocketMQMessageListener(topic = "${rocketmq.consumer.topic}",consumerGroup ="${rocketmq.consumer.group}")
//@Component
@Slf4j
public class ConsumerTagADemo implements RocketMQListener<String> {

    @Override
    public void onMessage(String o) {
        log.info("ConsumerTagADemo onMessage= {}",o);
        Message message = new Message();
        message.setDelayTimeSec(5);
    }
}

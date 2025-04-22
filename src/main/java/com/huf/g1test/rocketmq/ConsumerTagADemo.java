package com.huf.g1test.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = "${rocketmq.consumer.topic}",
        consumerGroup ="${rocketmq.consumer.group}",
        consumeMode = ConsumeMode.ORDERLY,
consumeThreadNumber = 1)
@Component
@Slf4j
public class ConsumerTagADemo implements RocketMQListener<MessageExt> {



    @Override
    public void onMessage(MessageExt messageExt) {
        int queueId = messageExt.getQueueId();
        String body = new String(messageExt.getBody());
        log.info("ConsumerTagADemo onMessage= {}  {}",queueId,body);
    }
}

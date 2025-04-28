package com.huf.g1test.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Slf4j
public class ProducerDemo {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送消息的实例
    public void sendMessage(String topic, String msg) {
        rocketMQTemplate.convertAndSend(topic,msg);
        //log.info("sendMessage : {}", msg);
    }

    public void sendOrderMessage(String msg){
        rocketMQTemplate.syncSendOrderly("OrderTopic",
                MessageBuilder.withPayload(msg).build(),
                "1");
    }

    public void sendAfterCommit(String topic,String msg){
        // 2. 注册事务提交后的回调
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 事务提交后发送消息
                    rocketMQTemplate.convertAndSend(topic, msg);
                }
            });
        }
    }
}

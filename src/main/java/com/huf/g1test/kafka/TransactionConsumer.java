package com.huf.g1test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.apache.kafka.common.TopicPartition;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TransactionConsumer {
    @KafkaListener(id = "webGroup3", topics = "topic_consume_for")
    public void listen(ConsumerRecord<String,String> record) {
        try {
            log.info("input value: {} {} {} {} {}",  record.value(),record.topic(), record.offset(), record.partition(),getMessageId(record));
            TimeUnit.SECONDS.sleep(10);
            log.info("finish  : {}", record.value());
            //ack.acknowledge();
        } catch (Exception e) {
            log.error("listen exception : ",e);
        }
    }
    private static String getMessageId(ConsumerRecord<String, String> record) {
        TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, record.offset(), 0L, 0L, 0L, 0, 0);
        return String.valueOf(recordMetadata.offset());
    }
}

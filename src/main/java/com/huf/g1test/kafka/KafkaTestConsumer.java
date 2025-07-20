package com.huf.g1test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTestConsumer {
    /**
     * 最基础的Spring Kafka单条消费demo，BATCH模式自动提交offset。
     * 配置：
     *   spring.kafka.consumer.enable-auto-commit: false
     *   spring.kafka.listener.ack-mode: batch
     *
     * 每条消息处理完后，Spring Kafka会自动在本批次全部处理完后提交offset。
     */
    @KafkaListener(id = "testGroupBatch", topics = "topic_test")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("[batch-ack] input value: {} {} {} {}", record.value(), record.topic(), record.offset(), record.partition());
            if (record.value().contains("fail")) {
            log.error("处理失败: {}，本批次不会提交offset，重启会重试本批次所有消息", record.value());
                throw new RuntimeException("处理失败");
            }
        try {
            Thread.sleep(1000); // 模拟慢处理
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("消息处理完成");
    }
}

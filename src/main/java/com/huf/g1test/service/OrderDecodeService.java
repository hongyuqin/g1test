package com.huf.g1test.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RefreshScope
public class OrderDecodeService {
    private String name;

    @Value("${name:hong}")
    private void setName(String name) {
        //log.info("name is : {}", name);
        this.name = name;
    }

    @Value("${order.decode.corePoolSize:4}")
    private int corePoolSize;
    @Value("${order.decode.maxPoolSize:8}")
    private int maxPoolSize;
    @Value("${order.decode.queueSize:100}")
    private int queueSize;

    private ThreadPoolExecutor executor;

    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        buildThreadPool();
        if (meterRegistry != null) {
            meterRegistry.gauge("order_decode_pool_active", executor, ThreadPoolExecutor::getActiveCount);
            meterRegistry.gauge("order_decode_pool_queue", executor, e -> e.getQueue().size());
            meterRegistry.gauge("order_decode_pool_completed", executor, ThreadPoolExecutor::getCompletedTaskCount);
        }
        log.info("线程池初始化: core={}, max={}, queue={}", corePoolSize, maxPoolSize, queueSize);
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        buildThreadPool();
        log.info("Nacos配置变更，线程池已重建: core={}, max={}, queue={}", corePoolSize, maxPoolSize, queueSize);
    }

    private synchronized void buildThreadPool() {
        if (executor != null) {
            executor.shutdown();
        }
        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize));
    }

    public List<String> decodeBatch(int count) {
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int orderId = i;
            futures.add(executor.submit(() -> {
                try {
                    // 模拟耗时任务
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return decodeOrder(orderId);
            }));
        }
        List<String> results = new ArrayList<>();
        for (Future<String> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                results.add("解码失败:" + e.getMessage());
            }
        }
        return results;
    }

    private String decodeOrder(int orderId) {
        try {
            // 模拟解码耗时
            Thread.sleep(300);
            // 模拟解析四要素
            String result = String.format("订单%d解码成功: 材质A, 型号B, 颜色C, 图片D", orderId);
            log.info(result);
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "订单" + orderId + "解码中断";
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void printName() {
        log.info("定时打印name: {}", name);
    }

    @PreDestroy
    public void shutdown() {
        if (executor != null) executor.shutdown();
    }
} 
package com.huf.g1test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态线程池服务类
 * 通过Nacos配置中心动态调整线程池参数
 */
@Slf4j
@Service
@RefreshScope
public class DynamicThreadPoolService {

    @Value("${thread.pool.core-size:2}")
    private int corePoolSize;

    @Value("${thread.pool.max-size:5}")
    private int maxPoolSize;

    @Value("${thread.pool.queue-capacity:100}")
    private int queueCapacity;

    @Value("${thread.pool.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    private ThreadPoolExecutor threadPool;
    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        createThreadPool();
        log.info("线程池初始化完成：coreSize={}, maxSize={}, queueCapacity={}, keepAliveSeconds={}",
                corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds);
    }

    /**
     * 监听配置刷新事件，重建线程池
     */
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        log.info("配置已刷新，更新线程池参数：coreSize={}, maxSize={}, queueCapacity={}, keepAliveSeconds={}",
                corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds);
        synchronized (lock) {
            ThreadPoolExecutor oldThreadPool = threadPool;
            // 创建新线程池
            createThreadPool();
            // 关闭旧线程池，但允许已提交任务完成
            if (oldThreadPool != null) {
                oldThreadPool.shutdown();
                try {
                    // 等待旧任务完成，但最多等待10秒
                    if (!oldThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        oldThreadPool.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    oldThreadPool.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 创建线程池
     */
    private void createThreadPool() {
        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("dynamic-pool-" + counter.getAndIncrement());
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 执行任务
     */
    public <T> Future<T> submit(Callable<T> task) {
        synchronized (lock) {
            return threadPool.submit(task);
        }
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        synchronized (lock) {
            threadPool.execute(task);
        }
    }

    /**
     * 获取线程池信息
     */
    public ThreadPoolInfo getThreadPoolInfo() {
        synchronized (lock) {
            ThreadPoolInfo info = new ThreadPoolInfo();
            info.setCorePoolSize(threadPool.getCorePoolSize());
            info.setMaximumPoolSize(threadPool.getMaximumPoolSize());
            info.setActiveCount(threadPool.getActiveCount());
            info.setPoolSize(threadPool.getPoolSize());
            info.setQueueSize(threadPool.getQueue().size());
            info.setQueueRemainingCapacity(threadPool.getQueue().remainingCapacity());
            info.setTaskCompletedCount(threadPool.getCompletedTaskCount());
            info.setKeepAliveSeconds(keepAliveSeconds);
            return info;
        }
    }

    /**
     * 定时打印线程池状态
     */
    @Scheduled(fixedRate = 60000)
    public void printThreadPoolStatus() {
        ThreadPoolInfo info = getThreadPoolInfo();
        log.info("线程池状态: {}", info);
    }

    @PreDestroy
    public void shutdown() {
        log.info("关闭线程池...");
        synchronized (lock) {
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
                try {
                    if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        threadPool.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    threadPool.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 线程池信息
     */
    public static class ThreadPoolInfo {
        private int corePoolSize;
        private int maximumPoolSize;
        private int activeCount;
        private int poolSize;
        private int queueSize;
        private int queueRemainingCapacity;
        private long taskCompletedCount;
        private long keepAliveSeconds;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public int getActiveCount() {
            return activeCount;
        }

        public void setActiveCount(int activeCount) {
            this.activeCount = activeCount;
        }

        public int getPoolSize() {
            return poolSize;
        }

        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }

        public int getQueueRemainingCapacity() {
            return queueRemainingCapacity;
        }

        public void setQueueRemainingCapacity(int queueRemainingCapacity) {
            this.queueRemainingCapacity = queueRemainingCapacity;
        }

        public long getTaskCompletedCount() {
            return taskCompletedCount;
        }

        public void setTaskCompletedCount(long taskCompletedCount) {
            this.taskCompletedCount = taskCompletedCount;
        }

        public long getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(long keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        @Override
        public String toString() {
            return "ThreadPoolInfo{" +
                    "corePoolSize=" + corePoolSize +
                    ", maximumPoolSize=" + maximumPoolSize +
                    ", activeCount=" + activeCount +
                    ", poolSize=" + poolSize +
                    ", queueSize=" + queueSize +
                    ", queueRemainingCapacity=" + queueRemainingCapacity +
                    ", taskCompletedCount=" + taskCompletedCount +
                    ", keepAliveSeconds=" + keepAliveSeconds +
                    '}';
        }
    }
} 
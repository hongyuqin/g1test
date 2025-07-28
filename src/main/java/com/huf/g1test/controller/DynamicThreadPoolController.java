package com.huf.g1test.controller;

import com.huf.g1test.service.DynamicThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 动态线程池控制器
 */
@Slf4j
@RestController
@RequestMapping("/threadpool")
public class DynamicThreadPoolController {

    @Autowired
    private DynamicThreadPoolService threadPoolService;

    /**
     * 获取线程池状态
     */
    @GetMapping("/status")
    public DynamicThreadPoolService.ThreadPoolInfo getStatus() {
        return threadPoolService.getThreadPoolInfo();
    }

    /**
     * 提交任务测试
     */
    @GetMapping("/test")
    public Map<String, Object> test(@RequestParam(defaultValue = "10") int count,
                                   @RequestParam(defaultValue = "1000") int taskTimeMillis) {
        log.info("收到测试请求: 任务数量={}, 任务执行时间={}ms", count, taskTimeMillis);
        
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        // 提交任务
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int taskId = i;
            futures.add(threadPoolService.submit(() -> {
                try {
                    // 模拟任务执行时间
                    int actualTime = taskTimeMillis + ThreadLocalRandom.current().nextInt(-200, 200);
                    actualTime = Math.max(100, actualTime);
                    Thread.sleep(actualTime);
                    return String.format("任务[%d]执行完成，耗时%dms，线程[%s]",
                            taskId, actualTime, Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return String.format("任务[%d]被中断", taskId);
                }
            }));
        }
        
        // 收集结果
        List<String> taskResults = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                taskResults.add(future.get(30, TimeUnit.SECONDS));
            } catch (Exception e) {
                taskResults.add("任务执行异常: " + e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        // 返回结果
        result.put("taskCount", count);
        result.put("totalTimeMs", endTime - startTime);
        result.put("results", taskResults);
        result.put("threadPoolStatus", threadPoolService.getThreadPoolInfo());
        
        return result;
    }
} 
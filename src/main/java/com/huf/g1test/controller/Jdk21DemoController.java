package com.huf.g1test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * JDK21 新特性演示控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/jdk21")
public class Jdk21DemoController {

    /**
     * 1. 虚拟线程 (Virtual Threads) - JDK21 核心特性
     */
    @GetMapping("/virtual-threads")
    public String virtualThreadsDemo() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 创建100个虚拟线程
            var futures = new CompletableFuture[100];
            for (int i = 0; i < 100; i++) {
                final int index = i;
                futures[i] = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(100); // 模拟IO操作
                        return "Task " + index + " completed";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "Task " + index + " interrupted";
                    }
                }, executor);
            }
            
            // 等待所有任务完成
            CompletableFuture.allOf(futures).join();
            return "Virtual threads demo completed! Created 100 virtual threads.";
        }
    }

    /**
     * 2. Pattern Matching for switch - JDK21 增强
     */
    @GetMapping("/pattern-matching/{type}")
    public String patternMatchingDemo(@PathVariable String type) {
        return switch (type) {
            case "string" -> "This is a string";
            case "number" -> "This is a number";
            case "boolean" -> "This is a boolean";
            case null -> "This is null";
            default -> "Unknown type: " + type;
        };
    }

    /**
     * 3. Record Patterns - JDK21 新特性
     */
    @GetMapping("/record-patterns")
    public String recordPatternsDemo() {
        // 定义记录类型
        record Point(int x, int y) {}
        record Circle(Point center, int radius) {}
        
        // 使用记录模式匹配
        Object shape = new Circle(new Point(0, 0), 5);
        
        return switch (shape) {
            case Circle(Point center, int radius) -> 
                "Circle with center at (" + center.x() + ", " + center.y() + ") and radius " + radius;
            default -> "Unknown shape";
        };
    }

    /**
     * 4. Sequenced Collections - JDK21 新特性
     */
    @GetMapping("/sequenced-collections")
    public Map<String, Object> sequencedCollectionsDemo() {
        // 有序集合的新方法
        List<String> list = List.of("first", "second", "third", "last");
        
        return Map.of(
            "first", list.getFirst(),
            "last", list.getLast(),
            "reversed", list.reversed(),
            "size", list.size()
        );
    }

    /**
     * 5. Unnamed Patterns and Variables - JDK21 新特性
     */
    @GetMapping("/unnamed-patterns")
    public String unnamedPatternsDemo() {
        record Person(String name, int age, String city) {}
        
        Person person = new Person("Alice", 30, "Beijing");
        
        // 使用未命名模式匹配
        return switch (person) {
            case Person(String name, int age, _) ->
                "Person " + name + " is " + age + " years old";
        };
    }

    /**
     * 6. 性能监控端点
     */
    @GetMapping("/performance")
    public Map<String, Object> performanceDemo() {
        Runtime runtime = Runtime.getRuntime();
        
        return Map.of(
            "availableProcessors", runtime.availableProcessors(),
            "totalMemory", runtime.totalMemory(),
            "freeMemory", runtime.freeMemory(),
            "maxMemory", runtime.maxMemory(),
            "currentTime", LocalDateTime.now()
        );
    }

    /**
     * 7. 简单的并发示例
     */
    @GetMapping("/concurrency")
    public String concurrencyDemo() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var future1 = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(100);
                    return "Task 1";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Task 1 interrupted";
                }
            }, executor);
            
            var future2 = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(200);
                    return "Task 2";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "Task 2 interrupted";
                }
            }, executor);
            
            var result1 = future1.get();
            var result2 = future2.get();
            
            return "Concurrency demo: " + result1 + ", " + result2;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 
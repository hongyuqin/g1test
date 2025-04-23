package com.huf.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 场景1:等待多个线程执行完
 */
@Slf4j
public class Scene1Test {
    private static CountDownLatch latch = new CountDownLatch(3);
    private static Runnable a = () -> {
        try {
            log.info("A begin");
            //睡30秒
            TimeUnit.SECONDS.sleep(30);
            log.info("A END");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }/*finally{
            latch.countDown();
        }*/
    };
    private static Runnable b = () -> {
        try {
            log.info("B begin");
            //睡30秒
            TimeUnit.SECONDS.sleep(20);
            log.info("B END");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }/*finally{
            latch.countDown();
        }*/
    };
    private static Runnable c = () -> {
        try {
            log.info("C begin");
            //睡30秒
            TimeUnit.SECONDS.sleep(10);
            log.info("C END");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }/*finally{
            latch.countDown();
        }*/
    };
    // 第一种方法：用join
    public static void main1(String[] args) throws Exception{
        //使用join
        Thread thread1 = new Thread(a);
        Thread thread2 = new Thread(b);
        Thread thread3 = new Thread(c);
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        log.info("JOIN WAIT END");
    }
    //第二种：用countdownlatch
    public static void main2(String[] args) throws Exception{
        //使用join
        Thread thread1 = new Thread(a);
        Thread thread2 = new Thread(b);
        Thread thread3 = new Thread(c);
        thread1.start();
        thread2.start();
        thread3.start();
        latch.await();
        log.info("COUNTDOWN LATCH WAIT END");
    }

    //第三种： 用线程池。awaitTerminate 等待结束
    public static void main3(String[] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(a);
        executor.submit(b);
        executor.submit(c);
        //不再接受新的任务
        executor.shutdown();
        //最多等待60s吧
        executor.awaitTermination(60, TimeUnit.SECONDS);
        log.info("ThreadPool WAIT END");
    }
    //第四种： 异步编程
    public static void main(String[] args) {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(a);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(b);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(c);
        CompletableFuture.allOf(future1, future2, future3).join();
        log.info("CompletableFuture WAIT END");
    }
}

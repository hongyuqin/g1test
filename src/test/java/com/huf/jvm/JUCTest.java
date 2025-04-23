package com.huf.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JUCTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        Runnable a = () -> {
            try {
                log.info("A begin");
                semaphore.acquire();
                log.info("A acquire success");
                //睡30秒
                TimeUnit.SECONDS.sleep(30);
                log.info("A release");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable b = () -> {
            try {
                log.info("B begin");
                semaphore.acquire();
                log.info("B acquire success");
                //睡30秒
                TimeUnit.SECONDS.sleep(30);
                log.info("B release");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(a).start();
        new Thread(b).start();

    }
}

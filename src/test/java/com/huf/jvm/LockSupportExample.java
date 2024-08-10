package com.huf.jvm;

import java.util.concurrent.locks.LockSupport;

public class LockSupportExample {

    public static void main(String[] args) {
        // 创建线程对象
        Thread thread = new Thread(() -> {
            System.out.println("线程开始执行，即将阻塞");
            LockSupport.park();  // 线程阻塞
            System.out.println("线程被唤醒，继续执行");
        });

        thread.start();

        try {
            Thread.sleep(3000);  // 主线程睡眠 3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程准备唤醒子线程");
        LockSupport.unpark(thread);  // 唤醒线程
    }
}
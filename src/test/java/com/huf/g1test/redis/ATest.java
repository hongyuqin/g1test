package com.huf.g1test.redis;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ATest {


    //三个线程交替打印0～100


    public static void main(String[] args) throws Exception{
        Semaphore a = new Semaphore(1);
        Semaphore b = new Semaphore(1);
        Semaphore c = new Semaphore(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        b.acquire();
        c.acquire();
        //A B C 3个线程
        Runnable aR = () -> {
            while(true) {
                try {
                    a.acquire();
                    System.out.println("aR : " + atomicInteger.addAndGet(1));
                    if (atomicInteger.get() >= 100) {
                        return;
                    }
                    b.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable bR = () -> {
            while(true) {
                try {
                    b.acquire();
                    System.out.println("bR : " + atomicInteger.addAndGet(1));
                    if (atomicInteger.get() >= 100) {
                        return;
                    }
                    c.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable cR = () -> {
            while(true) {
                try {
                    c.acquire();
                    System.out.println("cR : " + atomicInteger.addAndGet(1));
                    if (atomicInteger.get() >= 100) {
                        return;
                    }
                    a.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(aR).start();
        new Thread(bR).start();
        new Thread(cR).start();

        System.out.println("hello world");
    }
}

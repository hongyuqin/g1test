package com.huf.jvm;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    @Test
    public void test(){
        ThreadPoolExecutor service = new ThreadPoolExecutor(2, 4,
                5L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1));

        //比如执行3个
    }

    @Test
    public void testBlockingQueue() throws Exception {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        queue.take();
    }
}

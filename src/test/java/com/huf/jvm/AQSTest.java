package com.huf.jvm;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

public class AQSTest {
    @Test
    public void testLock(){
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }

}

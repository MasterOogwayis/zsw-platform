package com.zsw.tests;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 **/
@Slf4j
public class LockTests extends BaseCuratorTests {

    private Executor executor = ExecutorsUtils.create();


    @Test
    public void test() {
        InterProcessMutex lock = new InterProcessMutex(super.client, "/locks");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log.info("{} 尝试获得锁", Thread.currentThread().getName());
                try {
                    lock.acquire();
                    log.info("{} 获得锁成功", Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TimeUnit.SECONDS.sleep(4);
                    lock.release();
                    log.info("{} 释放锁", Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Thread - " + i).start();
        }
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

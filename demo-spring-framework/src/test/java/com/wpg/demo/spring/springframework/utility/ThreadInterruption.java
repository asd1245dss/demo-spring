package com.wpg.demo.spring.springframework.utility;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ChangWei Li
 * @version 2017-09-08 09:50
 */
public class ThreadInterruption {

    @Test
    public void test_interrupt_thrad() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (int i = 0; i < 100; i++) {
            executorService.execute(new InterrupedThread());
        }

        Thread.sleep(500);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

}

@Slf4j
class InterrupedThread implements Runnable {

    @Override
    public void run() {
        try {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            log.info("Current Thread sleep for 50 Seconds");
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            log.error("current thread is interrupted", e);
            // 抛出InterruptedException会重置Thread的中断状态
            // 再次设置当前Thread的中断状态
            Thread.currentThread().interrupt();
        }
    }
}

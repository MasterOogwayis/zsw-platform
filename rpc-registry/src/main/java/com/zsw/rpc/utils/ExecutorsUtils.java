package com.zsw.rpc.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ZhangShaowei on 2019/7/15 16:28
 **/
public class ExecutorsUtils {

    /**
     * 最大线程数:CPU核心数-N  1.计算密集型：N + 1   2.IO密集型：2N+1
     */
    private static Integer corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;


    private static Integer maxPoolSize = 50;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static Integer keepAliveSeconds = 60;

    /**
     * 队列最大长度 >=mainExecutor.maxSize
     */
    private static Integer queueCapacity = 500;

    public static Executor create() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("ThreadPool-Executor-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}

package com.zsw.rpc.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 由于 server 和 client 模式都要使用 zk 客户端，所以单独配置 zk client
 *
 * @author Administrator on 2019/6/8 20:47
 **/
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@PropertySource("classpath:application.properties")
public class RpcRegistryAutoConfiguration {


    @Value("${zookeeper.server.addresses}")
    private String zookeeperAddresses;

    @Value("${zookeeper.client.namespace}")
    private String namespace;

    @Bean
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistryCenter(zookeeperAddresses, namespace, this.executor());
    }



    /**
     * 最大线程数:CPU核心数-N  1.计算密集型：N + 1   2.IO密集型：2N+1
     */
    private Integer corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;


    private Integer maxPoolSize = 50;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private Integer keepAliveSeconds = 60;

    /**
     * 队列最大长度 >=mainExecutor.maxSize
     */
    private Integer queueCapacity = 500;

    /**
     * ExecutorService ??
     *
     * @return
     */
    public Executor executor() {
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

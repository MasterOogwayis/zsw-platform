package com.zsw.rpc.registry.stereotype;


import com.zsw.rpc.server.RpcServerConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 1. 初始化 zk 注册中心
 * 2. 扫描 @RpcClient 并注册代理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcServerConfigurationSelector.class)
public @interface EnableDiscoverClient {

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE;

}

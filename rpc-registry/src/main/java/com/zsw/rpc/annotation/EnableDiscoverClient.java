package com.zsw.rpc.annotation;


import com.zsw.rpc.client.RpcClientConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 1. 初始化 zk 注册中心
 * 2. 扫描 @RpcClient 并注册代理
 * @author ZhangShaowei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcClientConfigurationSelector.class)
public @interface EnableDiscoverClient {

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE;

}

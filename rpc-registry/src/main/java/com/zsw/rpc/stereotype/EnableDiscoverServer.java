package com.zsw.rpc.stereotype;


import com.zsw.rpc.server.RpcServerConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 1. 初始化 zk 注册中心
 * 2. 启动 netty 服务
 * 3. 扫描 @RpcService 并注册
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcServerConfigurationSelector.class)
public @interface EnableDiscoverServer {


    AdviceMode mode() default AdviceMode.PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE;

}

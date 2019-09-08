package com.zsw.rpc.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author ZhangShaowei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 实现接口
     *
     * @return
     */
    Class<?> impl();

    /**
     * 版本号
     *
     * @return
     */
    String version() default "";

}

package com.zsw.rpc.stereotype;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

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

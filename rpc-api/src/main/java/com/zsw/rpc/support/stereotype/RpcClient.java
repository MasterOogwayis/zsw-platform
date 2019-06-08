package com.zsw.rpc.support.stereotype;


import java.lang.annotation.*;

/**
 *
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {


    /**
     * 服务地址
     *
     * @return
     */
    String host();

    /**
     * port
     *
     * @return
     */
    int port();

    /**
     * 目标类，默认备注解的当前类
     *
     * @return
     */
    Class<?> target() default void.class;

    String version() default "";



}

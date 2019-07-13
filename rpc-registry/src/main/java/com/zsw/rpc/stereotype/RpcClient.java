package com.zsw.rpc.registry.stereotype;


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
     * 目标类，默认寻找当前类的实现
     *
     * @return
     */
    Class<?> target() default void.class;

    /**
     * 接口受限版本号
     *
     * @return
     */
    String version() default "";



}

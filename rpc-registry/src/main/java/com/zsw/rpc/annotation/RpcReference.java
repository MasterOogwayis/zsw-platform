package com.zsw.rpc.annotation;


import java.lang.annotation.*;

/**
 *
 * @author Administrator
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {


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

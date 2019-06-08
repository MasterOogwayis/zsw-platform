package com.zsw.rpc.server.stereotype;


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


    String api();

    String version() default "";

}

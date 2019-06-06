package com.zsw.rpc.api.impl;

import com.zsw.rpc.api.IServiceHello;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhangShaowei on 2019/6/6 13:29
 **/
@Slf4j
public class IServiceHelloImpl implements IServiceHello {

    @Override
    public String sayHello(String name) {
        log.info("Hello {}", name);
        return "Hello " + name;
    }
}

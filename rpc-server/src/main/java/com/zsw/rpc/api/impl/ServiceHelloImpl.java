package com.zsw.rpc.api.impl;

import com.zsw.rpc.api.ServiceHello;
import com.zsw.rpc.server.stereotype.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhangShaowei on 2019/6/6 13:29
 **/
@Slf4j
@RpcService(impl = ServiceHello.class)
public class ServiceHelloImpl implements ServiceHello {

    @Override
    public String sayHello(String name) {
        log.info("Hello {}", name);
        return "Hello " + name;
    }
}

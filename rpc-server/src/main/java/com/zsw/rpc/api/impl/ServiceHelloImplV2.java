package com.zsw.rpc.api.impl;

import com.zsw.rpc.api.ServiceHello;
import com.zsw.rpc.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhangShaowei on 2019/6/6 13:29
 **/
@Slf4j
@RpcService(impl = ServiceHello.class, version = "v2")
public class ServiceHelloImplV2 implements ServiceHello {

    @Override
    public String sayHello(String name) {
        log.info("V2 Hello {}", name);
        return "V2 Hello " + name;
    }
}

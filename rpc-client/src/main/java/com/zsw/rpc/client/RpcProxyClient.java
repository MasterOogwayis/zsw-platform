package com.zsw.rpc.client;

import java.lang.reflect.Proxy;

/**
 * @author ZhangShaowei on 2019/6/6 14:03
 **/
public class RpcProxyClient {


    @SuppressWarnings("unchecked")
    public <T> T createRpcClient(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RemoteInvocationHandler("127.0.0.1", 8088)
        );


    }


}

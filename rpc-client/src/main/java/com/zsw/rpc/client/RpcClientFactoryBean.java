package com.zsw.rpc.client;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author ZhangShaowei on 2019/6/6 14:55
 **/
@Setter
public class RpcClientFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;

    private String host;

    private int port;

    private String target;

    public RpcClientFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }



    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RpcClientProxy(this.host, this.port, this.target)
        );
    }

    @Override
    public Class<T> getObjectType() {
        return this.clazz;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
}

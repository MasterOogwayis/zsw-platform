package com.zsw.rpc.client;

import com.zsw.rpc.loadbalance.IRule;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @author Administrator
 **/
@Setter
public class RpcClientFactoryBean<T> implements FactoryBean<T> {

    private String serverName;

    @Autowired
    private IRule rule;

    private Class<T> clazz;

    private String target;

    private String version;

    public RpcClientFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RpcClientProxy(this.serverName, this.rule, this.target, this.version)
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

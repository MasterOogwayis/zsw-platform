package com.zsw.rpc.client;

import com.zsw.rpc.discovery.RegistryCenter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @author ZhangShaowei on 2019/6/6 14:55
 **/
@Setter
public class RpcClientFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;

    private String serverName;

    private String target;

    private String version;

    @Autowired
    private RegistryCenter registryCenter;

    public RpcClientFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }



    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new RpcClientProxy(this.registryCenter, this.serverName, this.target, this.version)
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

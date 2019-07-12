package com.zsw.rpc.client;

import com.zsw.rpc.discovery.RegistryCenter;
import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author ZhangShaowei on 2019/6/6 14:05
 **/
@Setter
public class RpcClientProxy implements InvocationHandler {

    public RpcClientProxy(RegistryCenter registryCenter, String serverName, String target, String version) {
        this.registryCenter = registryCenter;
        this.serverName = serverName;
        this.target = target;
        this.version = version;
    }

    String serverName;

    String target;

    String version;

    RegistryCenter registryCenter;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);
        }

        RpcResponse<?> response = this.rpcTransport(proxy, method, args);
        if (response.isSuccess()) {
            return response.getData();
        }
        throw new Exception(String.valueOf(response.getData()));
    }


    private RpcResponse<?> rpcTransport(Object proxy, Method method, Object[] args) throws Exception {
        RpcRequest request = RpcRequest.builder()
                .clazz(this.target)
                .method(method.getName())
                .params(args)
                .version(this.version)
                .build();
        return new RpcNetTransport(this.registryCenter.select(this.serverName)).send(request);
    }

    private boolean isDefaultMethod(Method method) {
        return ((method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC)
                && method.getDeclaringClass().isInterface();
    }


    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
                .newInstance(declaringClass,
                        MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }
}

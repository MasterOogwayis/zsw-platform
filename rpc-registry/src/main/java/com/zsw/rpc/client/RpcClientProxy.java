package com.zsw.rpc.client;

import com.zsw.rpc.remoting.RpcRequest;
import com.zsw.rpc.remoting.RpcResponse;
import com.zsw.rpc.loadbalance.IRule;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Administrator
 **/
@Setter
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    private String serverName;

    private IRule rule;

    private String target;

    private String version;


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
        return new RpcNetTransport(this.rule.select(this.serverName)).send(request);
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

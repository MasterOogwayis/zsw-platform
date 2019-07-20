package com.zsw.rpc.client;

import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Setter;
import org.springframework.lang.UsesJava7;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;

/**
 * @author ZhangShaowei on 2019/6/6 14:05
 **/
@Setter
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    private String host;

    private int port;

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
        Socket socket = new Socket(this.host, this.port);
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ){
            RpcRequest request = RpcRequest.builder()
                    .clazz(this.target)
                    .method(method.getName())
                    .params(args)
                    .version(this.version)
                    .build();
            oos.writeObject(request);
            oos.flush();
            return (RpcResponse<?>) ois.readObject();
        }
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
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
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;

    private int port;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);
        }

        RpcRequest request = RpcRequest.builder()
                .clazz(method.getDeclaringClass().getName())
                .method(method.getName())
                .params(args)
                .build();

        @Cleanup Socket socket = new Socket(this.host, this.port);

        @Cleanup ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(request);
        oos.flush();

        @Cleanup ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        @SuppressWarnings("unchecked")
        RpcResponse<String> response = (RpcResponse<String>) ois.readObject();
        return response.getData();
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

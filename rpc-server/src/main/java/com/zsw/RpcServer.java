package com.zsw;

import com.zsw.rpc.api.impl.IServiceHelloImpl;
import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ZhangShaowei on 2019/6/6 13:26
 **/
public class RpcServer {

    @SneakyThrows
    public static void main(String[] args) {

        @Cleanup ServerSocket serverSocket = new ServerSocket(8088);

        Socket accept = serverSocket.accept();

        @Cleanup ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());

        RpcRequest request = (RpcRequest) ois.readObject();


        Object invoke = invoke(request);

        RpcResponse<?> response = RpcResponse.builder().data(invoke.toString()).build();

        @Cleanup ObjectOutputStream oos = new ObjectOutputStream(accept.getOutputStream());
        oos.writeObject(response);
        oos.flush();


    }


    @SneakyThrows
    private static Object invoke(RpcRequest request) {
        Class[] types = new Class[request.getParams().length];

        for (int i = 0; i < request.getParams().length; i++) {
            types[i] = request.getParams()[i].getClass();
        }

        Class<?> clazz = Class.forName(request.getClazz());

        Method declaredMethod = clazz.getDeclaredMethod(request.getMethod(), types);

        return declaredMethod.invoke(new IServiceHelloImpl(), request.getParams());


    }


}

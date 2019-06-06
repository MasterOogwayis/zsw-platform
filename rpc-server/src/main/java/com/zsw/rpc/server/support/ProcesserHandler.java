package com.zsw.rpc.server.support;

import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author ZhangShaowei on 2019/6/6 14:14
 **/
@AllArgsConstructor
public class ProcesserHandler implements Runnable {

    Socket socket;

    Object target;

    @SneakyThrows
    @Override
    public void run() {
        try {
            @Cleanup ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            RpcRequest request = (RpcRequest) ois.readObject();
            Object invoke = this.invoke(request);
            RpcResponse response = RpcResponse.builder().data(invoke).build();
            @Cleanup ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
            oos.writeObject(response);
            oos.flush();
        } finally {
            this.socket.close();
        }
    }


    @SneakyThrows
    private Object invoke(RpcRequest request) {
        Class[] types = new Class[request.getParams().length];

        for (int i = 0; i < request.getParams().length; i++) {
            types[i] = request.getParams()[i].getClass();
        }

        Class<?> clazz = Class.forName(request.getClazz());

        Method declaredMethod = clazz.getDeclaredMethod(request.getMethod(), types);

        return declaredMethod.invoke(this.target, request.getParams());


    }


}

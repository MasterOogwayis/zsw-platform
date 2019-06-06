package com.zsw.rpc.client;

import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Setter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
        RpcResponse<String> response = (RpcResponse<String>) ois.readObject();
        return response.getData();
    }
}

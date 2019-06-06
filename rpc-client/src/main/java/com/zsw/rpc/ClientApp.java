package com.zsw.rpc;

import com.zsw.rpc.api.IServiceHello;
import com.zsw.rpc.client.RpcProxyClient;
import lombok.SneakyThrows;

/**
 * @author ZhangShaowei on 2019/6/6 13:35
 **/
public class ClientApp {

    @SneakyThrows
    public static void main(String[] args) {

        RpcProxyClient proxyClient = new RpcProxyClient();
        IServiceHello rpcClient = proxyClient.createRpcClient(IServiceHello.class);
        System.out.println(rpcClient.sayHello("Shaowei Zhang"));

    }

}

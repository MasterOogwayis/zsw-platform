package com.zsw.rpc.client;


import com.zsw.rpc.dto.RpcRequest;
import com.zsw.rpc.dto.RpcResponse;

/**
 * @author ZhangShaowei on 2019/7/12 14:32
 **/
public class RpcNetTransport extends NettyClient<RpcRequest, RpcResponse<Object>> {
    public RpcNetTransport(String serverAddress) {
        super(serverAddress);
    }

    public static void main(String[] args) {
        RpcResponse<Object> send = new RpcNetTransport("localhost:8082").send(RpcRequest.builder().build());
        System.out.println(send);
    }


}

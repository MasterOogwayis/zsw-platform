package com.zsw.rpc.client;


import com.zsw.rpc.remoting.RpcRequest;
import com.zsw.rpc.remoting.RpcResponse;

/**
 * @author Administrator
 **/
public class RpcNetTransport extends NettyClient<RpcRequest, RpcResponse<Object>> {
    public RpcNetTransport(String serverAddress) {
        super(serverAddress);
    }
}

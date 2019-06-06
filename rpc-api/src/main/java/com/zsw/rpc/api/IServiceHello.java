package com.zsw.rpc.api;

import com.zsw.rpc.support.stereotype.RpcClient;

/**
 * @author ZhangShaowei on 2019/6/6 13:27
 **/
@RpcClient(host = "127.0.0.1", port = 8088)
public interface IServiceHello {

    String sayHello(String name);

}

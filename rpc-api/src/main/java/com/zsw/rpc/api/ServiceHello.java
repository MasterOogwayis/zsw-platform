package com.zsw.rpc.api;

import com.zsw.rpc.support.stereotype.RpcClient;

/**
 * @author ZhangShaowei on 2019/6/6 13:27
 **/
@RpcClient
public interface ServiceHello {

    String sayHello(String name);

}

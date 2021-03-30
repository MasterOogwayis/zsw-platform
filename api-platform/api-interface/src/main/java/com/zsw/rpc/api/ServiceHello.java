package com.zsw.rpc.api;


import com.zsw.rpc.annotation.RpcReference;

/**
 * @author ZhangShaowei on 2019/6/6 13:27
 **/
@RpcReference
public interface ServiceHello {

    String sayHello(String name);

}

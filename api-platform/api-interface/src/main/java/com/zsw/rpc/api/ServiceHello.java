package com.zsw.rpc.api;


import com.zsw.rpc.annotation.RpcReference;

/**
 * @author Administrator
 **/
@RpcReference
public interface ServiceHello {

    String sayHello(String name);

}

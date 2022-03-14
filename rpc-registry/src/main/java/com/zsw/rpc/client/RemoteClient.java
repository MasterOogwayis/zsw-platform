package com.zsw.rpc.client;

/**
 * @author Administrator
 **/
public interface RemoteClient<T, R> {

    R send(T t);

}

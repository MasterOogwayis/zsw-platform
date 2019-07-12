package com.zsw.rpc.client;

/**
 * @author ZhangShaowei on 2019/7/12 14:11
 **/
public interface RemoteClient<T, R> {

    R send(T t);

}

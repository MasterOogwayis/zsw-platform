package com.zsw.rpc.registry;

/**
 * @author Administrator on 2019/7/13 15:11
 **/
@FunctionalInterface
public interface ServerChangeListener {

    void change(String serverName) throws Exception;

}

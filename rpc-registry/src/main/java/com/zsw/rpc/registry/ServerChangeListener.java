package com.zsw.rpc.registry;

/**
 * @author Administrator
 **/
@FunctionalInterface
public interface ServerChangeListener {

    void change(String serverName) throws Exception;

}

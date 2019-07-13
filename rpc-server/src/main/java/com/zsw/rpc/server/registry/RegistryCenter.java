package com.zsw.rpc.server.registry;

/**
 * 注册中心
 *
 * @author Administrator on 2019/7/13 12:51
 **/
public interface RegistryCenter {

    void registry(String serverName, String serverAddress);

}

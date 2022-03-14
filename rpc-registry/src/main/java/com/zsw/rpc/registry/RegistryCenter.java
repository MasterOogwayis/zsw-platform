package com.zsw.rpc.registry;

import java.util.List;

/**
 * 注册中心
 *
 * @author Administrator
 **/
public interface RegistryCenter {

    /**
     * 注册服务
     *
     * @param serverName  服务名
     * @param serverAddress 地址
     */
    void registry(String serverName, String serverAddress);

    /**
     * 拉取服务地址
     *
     * @param serverName 服务名
     * @return 地址集合
     */
    List<String> pull(String serverName);


    /**
     * 提供一个监听接口，当服务变化时触发listener
     *
     * @param serverName
     * @param listener
     */
    void watch(String serverName, ServerChangeListener listener);

}

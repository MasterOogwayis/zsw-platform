package com.zsw.rpc.discovery;

/**
 * 服务注册
 *
 * @author ZhangShaowei on 2019/7/12 9:32
 **/
public interface RegistryCenter {

    void registry(String serverName, String address);

    String select(String serviceName) throws Exception;

}

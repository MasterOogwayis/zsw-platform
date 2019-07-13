package com.zsw.rpc.server.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Administrator on 2019/7/13 12:52
 **/
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter, InitializingBean {

    String zookeeperAddresses;

    CuratorFramework client;


    @Override
    public void registry(String serverName, String serverAddress) {

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

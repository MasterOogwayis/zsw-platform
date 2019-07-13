package com.zsw.rpc.server.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Administrator on 2019/6/8 20:47
 **/
@PropertySource({"classpath:server.properties", "classpath:zookeeper.properties"})
@Configuration
public class RpcServerAutoConfiguration {

    @Value("${server.port}")
    private Integer port;

    @Value("${server.address}")
    private String address;


    @Value("${zookeeper.server.addresses}")
    private String zookeeperAddresses;


}

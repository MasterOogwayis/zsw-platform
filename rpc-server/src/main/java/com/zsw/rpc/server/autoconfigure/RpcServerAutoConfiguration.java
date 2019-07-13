package com.zsw.rpc.server.autoconfigure;

import com.zsw.rpc.server.RemoteServer;
import com.zsw.rpc.server.registry.RegistryCenter;
import com.zsw.rpc.server.registry.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;

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

    @Value("${zookeeper.client.namespace}")
    private String namespace;


    @Bean
    public RemoteServer remoteServer(RegistryCenter registryCenter) {
        return new RemoteServer(new InetSocketAddress(this.address, this.port), registryCenter);
    }

    @Bean
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistryCenter(zookeeperAddresses, namespace);
    }




}

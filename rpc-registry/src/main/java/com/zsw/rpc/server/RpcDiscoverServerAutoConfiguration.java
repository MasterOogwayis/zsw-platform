package com.zsw.rpc.server;

import com.zsw.rpc.registry.RegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;

import java.net.InetSocketAddress;

/**
 * @author Administrator
 **/
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@PropertySource("classpath:application.properties")
public class RpcDiscoverServerAutoConfiguration {

    @Value("${server.hostname}")
    private String hostname;

    @Value("${server.port}")
    private Integer port;

    @Bean
    public RpcDiscoverServer rpcDiscoverServer(RegistryCenter registryCenter) {
        return new RpcDiscoverServer(new InetSocketAddress(this.hostname, this.port), registryCenter);
    }

}

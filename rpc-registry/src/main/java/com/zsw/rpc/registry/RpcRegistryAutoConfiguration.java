package com.zsw.rpc.registry;

import com.zsw.rpc.utils.ExecutorsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;

/**
 * 由于 server 和 client 模式都要使用 zk 客户端，所以单独配置 zk client
 *
 * @author Administrator on 2019/6/8 20:47
 **/
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@PropertySource("classpath:application.properties")
public class RpcRegistryAutoConfiguration {


    @Value("${zookeeper.server.addresses}")
    private String zookeeperAddresses;

    @Value("${zookeeper.client.namespace}")
    private String namespace;

    @Bean
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistryCenter(zookeeperAddresses, namespace, ExecutorsUtils.create());
    }

}

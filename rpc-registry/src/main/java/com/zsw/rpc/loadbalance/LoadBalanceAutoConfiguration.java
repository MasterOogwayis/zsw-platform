package com.zsw.rpc.loadbalance;

import com.zsw.rpc.registry.RegistryCenter;
import org.springframework.context.annotation.Bean;

/**
 * @author Administrator
 **/
public class LoadBalanceAutoConfiguration {

    @Bean
    public IRule iRule(RegistryCenter registryCenter) {
        return new ZookeeperClientRule(registryCenter);
    }

}

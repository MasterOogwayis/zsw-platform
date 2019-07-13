package com.zsw.rpc.client;

import com.zsw.rpc.loadbalance.LoadBalanceAutoConfiguration;
import com.zsw.rpc.registry.RpcRegistryAutoConfiguration;
import com.zsw.rpc.stereotype.EnableDiscoverClient;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author Administrator on 2019/7/13 13:49
 **/
public class RpcClientConfigurationSelector extends AdviceModeImportSelector<EnableDiscoverClient> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{
                AutoConfiguredClientScannerRegistry.class.getName(),
                LoadBalanceAutoConfiguration.class.getName(),
                RpcRegistryAutoConfiguration.class.getName()};
    }
}

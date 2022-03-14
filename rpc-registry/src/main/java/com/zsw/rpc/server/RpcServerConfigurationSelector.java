package com.zsw.rpc.server;

import com.zsw.rpc.registry.RpcRegistryAutoConfiguration;
import com.zsw.rpc.annotation.EnableDiscoverServer;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author Administrator
 **/
public class RpcServerConfigurationSelector extends AdviceModeImportSelector<EnableDiscoverServer> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{
                RpcRegistryAutoConfiguration.class.getName(),
                RpcDiscoverServerAutoConfiguration.class.getName()
        };
    }
}

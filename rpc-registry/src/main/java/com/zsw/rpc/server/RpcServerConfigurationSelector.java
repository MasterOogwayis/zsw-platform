package com.zsw.rpc.server;

import com.zsw.rpc.registry.RpcRegistryAutoConfiguration;
import com.zsw.rpc.registry.stereotype.EnableDiscoverServer;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @author Administrator on 2019/7/13 13:49
 **/
public class RpcServerConfigurationSelector extends AdviceModeImportSelector<EnableDiscoverServer> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{RpcRegistryAutoConfiguration.class.getName()};
    }
}

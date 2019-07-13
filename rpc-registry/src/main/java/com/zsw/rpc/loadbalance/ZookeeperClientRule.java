package com.zsw.rpc.loadbalance;

import com.zsw.rpc.registry.RegistryCenter;
import com.zsw.rpc.registry.ServerChangeListener;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator on 2019/7/13 15:04
 **/
@RequiredArgsConstructor
public class ZookeeperClientRule implements IRule {

    private RegistryCenter registryCenter;

    private final Map<String, Server> INSTANCE = new ConcurrentHashMap<>();


    /**
     * 获取服务地址
     *
     * @param serverName
     * @return
     */
    @Override
    public String select(String serverName) {
        if (!INSTANCE.containsKey(serverName)) {
            this.update(serverName);
        }
        return INSTANCE.get(serverName).choose();
    }


    /**
     * @param serverName
     */
    private void addWatch(String serverName) {
        this.registryCenter.watch(serverName, path -> {
            this.update(serverName);
        });
    }

    /**
     * 加载 server 地址
     *
     * @param serverName
     */
    private void update(String serverName) {
        List<String> addresses = this.registryCenter.pull(serverName);
        INSTANCE.put(serverName, new Server(serverName, new RoundRobinLoadBalance(addresses)));
    }


}

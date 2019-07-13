package com.zsw.rpc.loadbalance;

import com.zsw.rpc.registry.RegistryCenter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator on 2019/7/13 15:04
 **/
@Slf4j
@AllArgsConstructor
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
        String address = INSTANCE.get(serverName).choose();
        log.debug("select: serverName = {}, address = {}", serverName, address);
        return address;
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

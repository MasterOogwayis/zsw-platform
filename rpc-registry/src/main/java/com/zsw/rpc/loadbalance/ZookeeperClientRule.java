package com.zsw.rpc.loadbalance;

import com.zsw.rpc.registry.RegistryCenter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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

    private final Map<String, Object> WATCHERS = new ConcurrentHashMap<>();


    /**
     * 获取服务地址
     *
     * @param serverName
     * @return
     */
    @Override
    public String select(String serverName) throws Exception {
        if (!INSTANCE.containsKey(serverName)) {
            this.update(serverName);
            this.addWatch(serverName);
        }
        String address = INSTANCE.get(serverName).choose();
        log.debug("select: serverName = {}, address = {}", serverName, address);
        return address;
    }


    /**
     * @param serverName
     */
    private void addWatch(String serverName) {
        if (!WATCHERS.containsKey(serverName)) {
            this.registryCenter.watch(serverName, path -> this.update(serverName));
            WATCHERS.put(serverName, true);
        }
    }

    /**
     * 加载 server 地址
     *
     * @param serverName
     */
    private void update(String serverName) throws Exception {
        List<String> addresses = this.registryCenter.pull(serverName);
        if (CollectionUtils.isEmpty(addresses)) {
            INSTANCE.remove(serverName);
            throw new Exception("Service not found with given name : " + serverName);
        } else {
            INSTANCE.put(serverName, new Server(serverName, new RoundRobinLoadBalance(addresses)));
        }
    }


}

package com.zsw.rpc.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用 zookeeper 注册
 *
 * @author ZhangShaowei on 2019/7/12 9:35
 **/
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter, InitializingBean {

    private static final String PATH_PREFIX = "/";

    public ZookeeperRegistryCenter(String serverAddresses, String namespace) {
        this.serverAddresses = serverAddresses;
        this.namespace = namespace;
    }


    String serverAddresses;

    String namespace;

    /**
     * zk 客户端
     */
    CuratorFramework client;

    /**
     * 缓存的 服务地址
     */
    Map<String, ILoadBalance> INSTANCE = new ConcurrentHashMap<>();


    @Override
    public void registry(String serverName, String address) {
        String serverPath = PATH_PREFIX + serverName;

        // 检查节点是否存在
        try {
            Stat stat = this.client.checkExists().forPath(serverPath);
            if (Objects.isNull(stat)) {
                this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serverPath);
            }

            // serverAddress: ip:port
            String addressPath = serverPath + PATH_PREFIX + address;
            // 服务注册创建临时节点
            this.client.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            log.info("服务注册成功：path = {}", addressPath);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public String select(String serviceName) throws Exception {
        if (INSTANCE.isEmpty()) {
            refreshInstance(serviceName);
            // 启动监听
            registerWatcher(serviceName);
        }
        String server = this.INSTANCE.get(serviceName).chooseServer();
        log.debug("LoadBalance: serverName = {}, serverAddress = {}", serviceName, server);
        return server;
    }


    /**
     * 初始化 zookeeper 客户端
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 启动 zk 客户端
        this.client = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(5 * 1000)
                .connectionTimeoutMs(10 * 1000)
                .connectString(serverAddresses)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(this.namespace)
                .build();
        this.client.start();
    }


    /**
     * 更新实例地址
     */
    private void refreshInstance(String serverName) throws Exception {
        String serverPath = PATH_PREFIX + serverName;
        List<String> addresses = this.client.getChildren().forPath(serverPath);
        INSTANCE.put(serverName, new RoundRobinLoadBalance(addresses));
    }


    private void registerWatcher(String serverName) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(this.client, PATH_PREFIX + serverName, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                log.info("服务端节点已变更：type = {}, path = {}, data = {}"
                        , event.getType(), event.getData().getPath(), event.getData());
                refreshInstance(serverName);
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
        pathChildrenCache.start();
    }

}

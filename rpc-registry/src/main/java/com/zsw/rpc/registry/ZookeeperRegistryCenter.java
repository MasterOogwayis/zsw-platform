package com.zsw.rpc.registry;

import lombok.SneakyThrows;
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

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator on 2019/7/13 12:52
 **/
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter, InitializingBean, Closeable {

    private static final String PATH_PREFX = "/";

    String zookeeperAddresses;

    String namespace;

    CuratorFramework client;

    public ZookeeperRegistryCenter(String zookeeperAddresses, String namespace) {
        this.zookeeperAddresses = zookeeperAddresses;
        this.namespace = namespace;
    }

    @Override
    @SneakyThrows
    public void registry(String serverName, String serverAddress) {
        String path = PATH_PREFX + serverName;
        Stat stat = this.client.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            String forPath = this.client.create().creatingParentsIfNeeded().forPath(path);
            log.info("create path: {}", forPath);
        }
        String forPath = this.client.create().withMode(CreateMode.EPHEMERAL).forPath(path + PATH_PREFX + serverAddress);
        log.info("server registed: serverName = {}, serverAddress = {}, path = {}", serverName, serverAddress, forPath);
    }

    @Override
    @SneakyThrows
    public List<String> pull(String serverName) {
        String path = PATH_PREFX + serverName;
        return this.client.getChildren().forPath(path);
    }

    @Override
    @SneakyThrows
    public void watch(String serverName, ServerChangeListener listener) {
        String path = PATH_PREFX + serverName;
        PathChildrenCache childrenCache = new PathChildrenCache(this.client, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                log.warn("服务器变更：path = {}, type = {}", pathChildrenCacheEvent.getData().getPath(), pathChildrenCacheEvent.getType());
                listener.change(pathChildrenCacheEvent.getData().getPath());
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        childrenCache.start();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(10 * 1000)
                .sessionTimeoutMs(60 * 1000)
                .connectString(this.zookeeperAddresses)
                .namespace(this.namespace)
                .build();
        this.client.start();
        log.info("zookeeper 已连接，addresses = {}", this.zookeeperAddresses);
    }


    @Override
    public void close() throws IOException {
        this.client.close();
    }
}

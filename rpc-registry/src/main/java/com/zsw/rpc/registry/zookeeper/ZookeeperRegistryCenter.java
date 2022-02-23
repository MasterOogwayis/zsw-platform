package com.zsw.rpc.registry.zookeeper;

import com.zsw.rpc.registry.RegistryCenter;
import com.zsw.rpc.registry.ServerChangeListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.listen.StandardListenerManager;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Administrator on 2019/7/13 12:52
 **/
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter, InitializingBean, Closeable {

    private static final String PATH_PREFX = "/";

    private final String zookeeperAddresses;

    private final String namespace;

    private final Executor executor;

    CuratorFramework client;

    public ZookeeperRegistryCenter(String zookeeperAddresses, String namespace, Executor executor) {
        this.zookeeperAddresses = zookeeperAddresses;
        this.namespace = namespace;
        this.executor = executor;
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
                listener.change(pathChildrenCacheEvent.getData().getPath());
                log.warn("服务器变更：path = {}, type = {}", path, pathChildrenCacheEvent.getType());
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener, this.executor);
        childrenCache.start();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(15000)
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

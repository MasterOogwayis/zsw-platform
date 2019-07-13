package com.zsw.rpc.server.registry;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * @author Administrator on 2019/7/13 12:52
 **/
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter, InitializingBean {

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
        String forPath = this.client.create().creatingParentsIfNeeded().forPath(path + PATH_PREFX + serverAddress);
        log.info("server registed: serverName = {}, serverAddress = {}, path = {}", serverName, serverAddress, forPath);
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
}

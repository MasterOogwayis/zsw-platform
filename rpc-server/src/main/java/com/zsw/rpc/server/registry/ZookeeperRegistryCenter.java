package com.zsw.rpc.server.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

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
            String forPath = this.client.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            log.info("服务注册成功：path = {}", forPath);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

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
}

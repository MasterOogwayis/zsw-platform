package com.zsw.demo;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ZhangShaowei on 2019/7/11 14:05
 **/
@Slf4j
public class CuratorTests {

    private static final String SERVICES = "192.168.137.100:2181";

    private CuratorFramework client;

    @Before
    public void before() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(SERVICES)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();


        //CRUD
//        client.create();
        //修改
//        client.setData();
        // 删除
//        client.delete() ;
        //查询
//        client.getData();
    }


    /**
     * 创建永久节点
     */
    @Test
    @SneakyThrows
    public void testCreate() {
        String path = this.client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/data/program", "test".getBytes());
        log.debug("create: {}", path);
    }

    @Test
    @SneakyThrows
    public void testSetData() {
        Stat stat = this.client.setData().forPath("/data/program", "Shaowei Zhang".getBytes());
        log.debug("setData： stat = {}", stat);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        Void aVoid = this.client.delete().forPath("/data/program");
        log.debug("delete: void = {}", aVoid);
    }


    /**
     * 配置中心 创建、修改、删除
     */
    @Test
    @SneakyThrows
    public void testNodeCache() {
        NodeCache nodeCache = new NodeCache(this.client, "/watcher", false);
        NodeCacheListener listener = () -> {
            log.debug("Received node changed.");
            log.debug("path={}, data={}", nodeCache.getCurrentData().getPath(), nodeCache.getCurrentData().getData());
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();

        int read = System.in.read();
    }


    @Test
    @SneakyThrows
    public void testChildrenListener() {
        PathChildrenCache childrenCache = new PathChildrenCache(this.client, "/watcher", false);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                log.info("type={}, data={}", pathChildrenCacheEvent.getType(), pathChildrenCacheEvent.getData());
            }
        };
        childrenCache.getListenable().addListener(listener);
        childrenCache.start();

        int read = System.in.read();


    }


}

package com.zsw.tests;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 **/
@Slf4j
public class CuratorTests {

    CuratorFramework client;

    String path = "/temp";

    @Before
    public void before() {
        this.client = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(10 * 1000)
                .namespace("hello")
                .connectString(ZookeeperConfig.ZOOKEEPER_ADDRESSES)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
    }


    @Test
    @SneakyThrows
    public void testCreate() {
        String forPath = this.client.create().creatingParentsIfNeeded().forPath("/temp/config", "data".getBytes());
        log.info("forPath = {}", forPath);
    }

    @Test
    @SneakyThrows
    public void testGet() {
        byte[] data = this.client.getData().forPath("/temp/config");
        log.info("forPath = {}", new String(data));
    }

    @Test
    @SneakyThrows
    public void testWatch() {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(this.client, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

                log.info("node changed: type = {}, path = {}, data = {}", event.getType(), event.getData().getPath(), event.getData().getData());

            }
        };

        pathChildrenCache.getListenable().addListener(listener);

        pathChildrenCache.start();

        System.in.read();

    }


    @Test
    @SneakyThrows
    public void testACL() {
        List<ACL> acls = new ArrayList<>();

        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("auth", ""));
        acls.add(acl);


        Stat stat = this.client.setACL().withACL(acls).forPath("/temp");

        log.info("stat = {}", stat);


    }


}

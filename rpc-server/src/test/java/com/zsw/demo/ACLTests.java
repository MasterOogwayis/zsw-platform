package com.zsw.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author ZhangShaowei on 2019/7/11 15:55
 **/
@Slf4j
public class ACLTests {

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
    }


    @Test
    @SneakyThrows
    public void testACLCreate() {
        List<ACL> list = new ArrayList<>();

        ACL acl = new ACL(ZooDefs.Perms.READ, new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);

        String path = this.client.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/temp");

        log.debug("create path: {}", path);

    }

    /**
     * 创建 ACL 节点
     */
    @Test
    @SneakyThrows
    public void testCreateWithAcl() {
        String path = "/temp";
        byte[] data = "data".getBytes();
        List<ACL> acls = new ArrayList<>();
        Id user = new Id("digest", DigestAuthenticationProvider.generateDigest("user:123456"));
        Id admin = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin"));

        acls.add(new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, user));
        acls.add(new ACL(ZooDefs.Perms.ALL, admin));

        String forPath = this.client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
                .withACL(acls, true)
                .forPath(path, data);


        log.info("create with ACLS: forPath = {}, ACLS = {}", forPath, acls);
    }


    @Test
    @SneakyThrows
    public void testACLGet() {
        String path = "/temp";
        CuratorFramework client1 = CuratorFrameworkFactory.builder()
                .authorization("digest", "user:123456".getBytes())
                .connectString(SERVICES)
                .sessionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client1.start();

        byte[] bytes = client1.getData().forPath(path);
        List<ACL> acls = client1.getACL().forPath(path);

        log.info("data = {}, acls = {}", new String(bytes), acls);

    }


    @Test
    @SneakyThrows
    public void getAcls() {
        String path = "/temp";

        List<ACL> acls = this.client.getACL().forPath(path);

        log.info("path = {}, acls = {}", path, acls);

    }


}

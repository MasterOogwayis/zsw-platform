package com.zsw.tests;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;

/**
 * @author Administrator
 **/
public class BaseCuratorTests {

    protected CuratorFramework client;


    @Before
    public void before() {
        this.client = CuratorFrameworkFactory.builder()
                .connectionTimeoutMs(10 * 1000)
                .connectString(ZookeeperConfig.ZOOKEEPER_ADDRESSES)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
    }

}

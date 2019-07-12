package com.zsw.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;

/**
 * @author ZhangShaowei on 2019/7/11 15:55
 **/
public class BaseTests {

    private static final String SERVICES = "192.168.137.100:2181";

    protected CuratorFramework client;

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

}

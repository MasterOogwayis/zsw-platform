package com;

import com.zsw.rpc.api.ServiceHello;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author ZhangShaowei on 2019/6/6 14:52
 **/
@Slf4j
public class ClientTests {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(15000)
                .connectString("localhost:2181")
                .namespace("registry")
                .build();
        client.start();
        List<String> strings = client.getChildren().forPath("/" + ServiceHello.class.getName());

        strings.forEach(System.out::println);


    }


}

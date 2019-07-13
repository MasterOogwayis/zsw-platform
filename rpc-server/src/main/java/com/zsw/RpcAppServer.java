package com.zsw;

import com.zsw.rpc.registry.server.RpcDiscoverServer;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ZhangShaowei on 2019/6/6 13:26
 **/
public class RpcAppServer {

    @SneakyThrows
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.zsw.rpc");
        RpcDiscoverServer server = applicationContext.getBean(RpcDiscoverServer.class);


        System.in.read();

    }


}

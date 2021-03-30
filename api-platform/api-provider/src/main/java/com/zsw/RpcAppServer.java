package com.zsw;

import com.zsw.rpc.annotation.EnableDiscoverServer;
import com.zsw.rpc.server.RpcDiscoverServer;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhangShaowei on 2019/6/6 13:26
 **/
@Configuration
@EnableDiscoverServer
@ComponentScan("com.zsw.rpc")
public class RpcAppServer {

    @SneakyThrows
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcAppServer.class);
        RpcDiscoverServer server = applicationContext.getBean(RpcDiscoverServer.class);


        System.in.read();

    }


}

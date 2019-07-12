package com.zsw;

import com.zsw.rpc.server.RemoteServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ZhangShaowei on 2019/6/6 13:26
 **/
@Slf4j
public class RpcAppServer {

    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.zsw.rpc");
        RemoteServer server = applicationContext.getBean(RemoteServer.class);
        server.start();
    }


}

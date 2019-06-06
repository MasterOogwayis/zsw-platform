package com.zsw.rpc.server;

import com.zsw.rpc.api.ServiceHello;
import com.zsw.rpc.api.impl.ServiceHelloImpl;
import com.zsw.rpc.server.support.ProcesserHandler;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhangShaowei on 2019/6/6 14:13
 **/
@Slf4j
public class RemoteServer {

    ExecutorService executor = Executors.newCachedThreadPool();

    @SneakyThrows
    public void startup() {

        ServiceHello target = new ServiceHelloImpl();

        @Cleanup ServerSocket serverSocket = new ServerSocket(8088);

        for (; ; ) {
            this.executor.execute(new ProcesserHandler(serverSocket.accept(), target));
            log.info("A client has connected to server");
        }

    }


}

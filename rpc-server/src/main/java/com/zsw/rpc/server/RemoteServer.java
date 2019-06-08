package com.zsw.rpc.server;

import com.zsw.rpc.server.support.ProcesserHandler;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhangShaowei on 2019/6/6 14:13
 **/
@Slf4j
@Service
public class RemoteServer implements ApplicationContextAware, InitializingBean {

    ExecutorService executor = Executors.newCachedThreadPool();

    ApplicationContext applicationContext;

    @SneakyThrows
    public void startup() {

        @Cleanup ServerSocket serverSocket = new ServerSocket(8088);

        for (; ; ) {
            this.executor.execute(new ProcesserHandler(serverSocket.accept(), this.applicationContext));
            log.info("a client has connected to server");
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

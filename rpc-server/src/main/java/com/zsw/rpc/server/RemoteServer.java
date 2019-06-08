package com.zsw.rpc.server;

import com.zsw.rpc.server.stereotype.RpcService;
import com.zsw.rpc.server.support.ProcesserHandler;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhangShaowei on 2019/6/6 14:13
 **/
@Slf4j
@Service
public class RemoteServer implements ApplicationContextAware, InitializingBean {

    Map<String, Object> handlerMappings = new HashMap<>();

    ExecutorService executor = Executors.newCachedThreadPool();

    ApplicationContext applicationContext;

    @SneakyThrows
    public void publisher(int port) {

        @Cleanup ServerSocket serverSocket = new ServerSocket(port);

        for (; ; ) {
            this.executor.execute(new ProcesserHandler(serverSocket.accept(), this.handlerMappings));
            log.info("a client has connected to server");
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)) {
            log.warn("未找到可发布的 RpcService");
            return;
        }
        beansWithAnnotation.forEach((key, value) -> {
            RpcService rpcService = value.getClass().getAnnotation(RpcService.class);
            this.handlerMappings.put(rpcService.api() + "#" + rpcService.version(), value);
        });
    }
}

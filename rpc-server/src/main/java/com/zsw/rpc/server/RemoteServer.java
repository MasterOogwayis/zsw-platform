package com.zsw.rpc.server;

import com.zsw.rpc.server.registry.RegistryCenter;
import com.zsw.rpc.server.stereotype.RpcService;
import com.zsw.rpc.server.support.ProcesserHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhangShaowei on 2019/6/6 14:13
 **/
@Slf4j
@AllArgsConstructor
public class RemoteServer implements ApplicationContextAware, InitializingBean {

    private final Map<String, Object> handlerMappings = new HashMap<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

//    private final ApplicationContext applicationContext;

    private String address;

    private int port;

    private RegistryCenter registryCenter;

//    @SneakyThrows
//    public void publisher() {
//
//        @Cleanup ServerSocket serverSocket = new ServerSocket(this.port);
//
//        for (; ; ) {
//            this.executor.execute(new ProcesserHandler(serverSocket.accept(), this.handlerMappings));
//            log.info("a client has connected to server");
//        }
//
//    }


    /**
     * 在这里启动 zk 服务会阻塞 容器的初始化， or 使用异步
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {


    }

    /**
     * 启动服务器
     */
    public void start() throws InterruptedException {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                .addLast(new ObjectEncoder())
                                .addLast(new ProcesserHandler(handlerMappings));
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(this.address, this.port)).sync();
        log.info("服务器已启动...");
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)) {
            log.warn("未找到可发布的 RpcService");
            return;
        }
        beansWithAnnotation.forEach((key, value) -> {
            RpcService rpcService = value.getClass().getAnnotation(RpcService.class);
            String serviceName = rpcService.impl().getName();
            String version = rpcService.version(); //拿到版本号
            if (!StringUtils.isEmpty(version)) {
                serviceName += "-" + version;
            }
            this.handlerMappings.put(serviceName, value);
            this.registryCenter.registry(serviceName, this.address + ":" + this.port);
        });
    }
}

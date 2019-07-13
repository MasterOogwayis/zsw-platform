package com.zsw.rpc.server;

import com.zsw.rpc.registry.RegistryCenter;
import com.zsw.rpc.stereotype.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.Lifecycle;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator on 2019/7/13 14:01
 **/
@Slf4j
public class RpcDiscoverServer implements ApplicationContextAware, InitializingBean, Runnable {

    private InetSocketAddress address;

    private RegistryCenter registryCenter;

    private Map<String, Object> handlerMappings = new HashMap<>();

    public RpcDiscoverServer(InetSocketAddress address, RegistryCenter registryCenter) {
        this.address = address;
        this.registryCenter = registryCenter;
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
            String serverName = rpcService.impl().getName() + (StringUtils.isEmpty(rpcService.version()) ? "" : ("-" + rpcService.version()));
            this.handlerMappings.put(serverName, value);
            String address = this.address.getHostName() + ":" + this.address.getPort();
            this.registryCenter.registry(serverName, address);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注意要异步运行，不然会阻塞容器的初始化
        new Thread(this).start();
    }

    @Override
    public void run() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectDecoder(Integer.MAX_VALUE, null))
                                .addLast(new ObjectEncoder())
                                .addLast(new ProcessorHandler(handlerMappings));

                    }
                });

        ChannelFuture channelFuture;
        try {
            channelFuture = serverBootstrap.bind(address).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

package com.zsw.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author ZhangShaowei on 2019/7/12 14:11
 **/
@Slf4j
@ChannelHandler.Sharable
public abstract class NettyClient<T, R> extends SimpleChannelInboundHandler<R> implements RemoteClient<T, R> {

    private String serverAddress;

    public NettyClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    private R result;

    @Override
    public R send(T request) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                .addLast(new ObjectEncoder())
                                .addLast(NettyClient.this);
                    }
                });
        String[] split = serverAddress.split(":");
        InetSocketAddress address = new InetSocketAddress(split[0], Integer.valueOf(split[1]));
        try {
            ChannelFuture channelFuture = bootstrap.connect(address).sync();
            channelFuture.channel().writeAndFlush(request).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

        return this.result;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, R msg) throws Exception {
        this.result = msg;
    }
}

package com.wpg.demo.spring.netty.utility;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import static io.netty.channel.ChannelOption.SO_BACKLOG;
import static io.netty.handler.logging.LogLevel.DEBUG;

/**
 * @author ChangWei Li
 * @version 2017-09-09 22:19
 */
public class NettyServer extends ServerAdapter {

    public static void main(String[] args) {
        new NettyServer().init();
    }

    @Override
    public void init() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(eventLoopGroup, workerLoopGroup)
                .channel(NioServerSocketChannel.class)
                .option(SO_BACKLOG, 100)
                .handler(new LoggingHandler(DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();

                        channelPipeline.addLast(new NettyServerConnector());
                    }
                });

            ChannelFuture channelFuture = serverBootstrap.bind(DEFAULT_SERVER_PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                workerLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

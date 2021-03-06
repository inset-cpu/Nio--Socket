package com.yr.nitty.nitty4.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class Nittyserver {
    private static final int port = 8080;
    public static void main(String[] args) throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup(1);
        EventLoopGroup worker= new NioEventLoopGroup();
        ServerBootstrap boss = new ServerBootstrap();
        boss.group(group, worker)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 100)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ByteArrayEncoder(), new EvenServer());
                }
            });
        ChannelFuture future = boss.bind(port).sync();
        future.channel().closeFuture().sync();
    }
}

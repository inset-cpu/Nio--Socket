package com.yr.nitty.nitty4.Chilent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;

public class NIttychilent{
    private static String host ="192.168.1.125";
    private static int port =8080;
    private static String path="D:\\学习文档\\zj\\notes\\linux\\linuxIso\\ubuntu-18.04.2-desktop-amd64.iso";
    public static void main(String[] args) throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap boos  = new Bootstrap();
            boos.group(group);
            boos.channel(NioSocketChannel.class);
            boos.option(ChannelOption.TCP_NODELAY,true);
            boos.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ByteArrayEncoder());
                    ch.pipeline().addLast(new ChunkedWriteHandler());
                    ch.pipeline().addLast(new EvenChenit(new File(path).getName(),path));
                }
            });
            ChannelFuture future =boos.connect(host,port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}

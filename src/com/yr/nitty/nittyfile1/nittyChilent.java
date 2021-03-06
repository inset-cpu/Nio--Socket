package com.yr.nitty.nittyfile1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

public class nittyChilent {
    public void fuck(int port, String host,FileContent fileContent) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap boss = new Bootstrap();
            boss.group(group);
            boss.channel(NioSocketChannel.class);
            boss.option(ChannelOption.TCP_NODELAY, true);
            boss.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                    ch.pipeline().addLast(new FileNittychilent(fileContent));
                }
            });
            ChannelFuture future = boss.connect(host,port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        FileContent fileContent =  new FileContent();
        File file = new File("D:\\学习文档\\zj\\notes\\面试总结.docx");
        String fileName = file.getName();
        fileContent.setFile(file);
        fileContent.setFileName(fileName);
        fileContent.setStartPos(0);
        new nittyChilent().fuck(5555,"192.168.1.125",fileContent);
    }
}

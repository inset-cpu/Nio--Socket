package com.yr.nitty.NittyFile3.Chilent;

import com.yr.nitty.NittyFile3.Encoder;
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


public class Chilent {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap boos = new Bootstrap();
            boos.group(group);
            boos.channel(NioSocketChannel.class);
            boos.option(ChannelOption.TCP_NODELAY,true);
            boos.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast ( new ByteArrayEncoder(),
                                    new ChunkedWriteHandler(),
                                    new Encoder(),
                                    new EvenChilent());
                }
            });
            ChannelFuture future = boos.connect("192.168.1.125",8080).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}

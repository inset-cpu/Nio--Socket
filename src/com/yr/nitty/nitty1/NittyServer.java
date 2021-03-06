package com.yr.nitty.nitty1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NittyServer {
    private static final int port = 8050;
    public NittyServer(){

    }

    public static void main(String[] args) throws Exception{
        new NittyServer().stai();
    }
    public void stai()throws  Exception{
        ServerBootstrap serverbootstrap = new ServerBootstrap();
        EventLoopGroup eventExecutors =new NioEventLoopGroup();

        try{
            serverbootstrap.group(eventExecutors);
            serverbootstrap.channel(NioServerSocketChannel.class);
            serverbootstrap.localAddress(port);
            serverbootstrap.childHandler(new AA());
            ChannelFuture channelFuture = serverbootstrap.bind().sync();
            System.out.println(NittyServer.class.getName() + " *************** " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e ){
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
    private class AA extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new EvenNittyServer());
        }
    }
}

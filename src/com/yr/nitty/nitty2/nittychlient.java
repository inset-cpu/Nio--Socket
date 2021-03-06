package com.yr.nitty.nitty2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class nittychlient {

    public void mg(int port,String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new cc());
            ChannelFuture f = bootstrap.connect(host, port).sync();// 发起异步连接操作
            f.channel().closeFuture().sync();//等待客户端链路关闭
        }catch (Exception e){
          e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
    private class cc extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new EvenNittychenit());
        }
    }
    public static void main(String[] args) throws Exception {
        new nittychlient().mg(6666, "192.168.1.125");
    }
}

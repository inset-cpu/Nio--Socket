package com.yr.nitty.nitty1;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


import java.net.InetSocketAddress;

public class NittyChilet {
    private String host ;
    private int port ;
    public NittyChilet (String host , int port){
        this.host = host;
        this.port = port;
    }
    public void star() {
        EventLoopGroup boss = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(boss);

            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress(host,port));
            bootstrap.handler(new A());
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public static void main(String[] args) {
    new NittyChilet("192.168.1.125",8050).star();
    }

    //内部类
    private class  A  extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new EvenNttyChilent());

        }
    }
}

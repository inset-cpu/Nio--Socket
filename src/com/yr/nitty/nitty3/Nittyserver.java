package com.yr.nitty.nitty3;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Nittyserver {
    public void bind(int port) throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();//异步线程池   引导辅助程序
        EventLoopGroup work = new NioEventLoopGroup();//异步线程池  通过nio方式来接收连接和处理连接
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,work);//添加工作线程组
            bootstrap.channel(NioServerSocketChannel.class);//设置管道模式  设置nio类型的channel
            bootstrap.option(ChannelOption.SO_BACKLOG,1024);//配置BLOCK大小
            bootstrap.childHandler(new AA());//有连接到达时会创建一个channel
            ChannelFuture f = bootstrap.bind(port).sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭


        }finally {
            boss.shutdownGracefully().sync();
            work.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }
    public static void main(String[] args) throws Exception{
        new Nittyserver().bind(6666);
    }

    private class AA extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));///自动完成标识符分隔解码器
            ch.pipeline().addLast(new StringDecoder());//消息转成String解码器  用来处理粘包与半包，使用换行符
            ch.pipeline().addLast(new EvenNittyserver());
        }
    }
}

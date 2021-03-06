package com.yr.nitty.nitty6;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Nittychlient {

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
            //ch.pipeline().addLast(new LineBasedFrameDecoder(3072));///自动完成标识符分隔解码器
//            ByteBuf delimiter = Unpooled.copiedBuffer("!@$".getBytes());
            ch.pipeline().addLast(new FixedLengthFrameDecoder(30));
            ch.pipeline().addLast(new StringDecoder());//消息转成String解码器  用来处理粘包与半包，使用换行符
            ch.pipeline().addLast(new EvenNittychenit());
        }
    }
    public static void main(String[] args) throws Exception {
        new Nittychlient().mg(6666, "192.168.1.125");
    }
}

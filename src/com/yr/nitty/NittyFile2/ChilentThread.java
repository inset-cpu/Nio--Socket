package com.yr.nitty.NittyFile2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
public class ChilentThread extends Thread{
    public String path;
    private int make ;
    public ChilentThread(String path, int make) {
        this.path=path;
        this.make=make;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new Encoder(),
                            new ByteArrayEncoder(),
                            new ChunkedWriteHandler(),
                            new StringDecoder(),
                            new EvenChilent(path, make));
                }
            });
            ChannelFuture f = b.connect("192.168.1.125", 8080).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }

    }
}

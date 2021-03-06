package com.yr.nitty.nitty4.Chilent;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;

public class EvenChenit extends ChannelInboundHandlerAdapter {
    private String path;
    private String fileNames;
    public EvenChenit(String fileName,String paths) {
        this.fileNames = fileName;
        this.path = paths;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(fileNames.getBytes(CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RandomAccessFile files =null;
        try{
            files = new RandomAccessFile(path,"r");
        }catch(Exception e) {
        e.printStackTrace();
        }
        try {
            ctx.writeAndFlush(new ChunkedFile(files)).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.channel().close();
                }
            });
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        cause.printStackTrace();
    }
}

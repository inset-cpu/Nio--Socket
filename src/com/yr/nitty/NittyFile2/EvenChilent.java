package com.yr.nitty.NittyFile2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;

public class EvenChilent extends ChannelInboundHandlerAdapter {
    private String basepath = "D:\\学习文档\\zj\\cours";
    private String path;  //文件路径
    private int make;
    public EvenChilent(String path, int make) {
        this.path=path;
        this.make= make;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message =new Message();
        message.setMark(make);
        if(make == 1){
            message.setFileLength(new File(path).length());
        }
        message.setFilePath(path.replace(basepath,""));
        message.setFilePathLength(path.replace(basepath,"").getBytes().length);
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String test =(String)msg;
        if(test.equals("b")){
            ctx.close();
        }else if(test.equals("a")){
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(path,"r");
            }catch (Exception e ){
                e.printStackTrace();
            }
            try {
                ctx.writeAndFlush(new ChunkedFile(randomAccessFile)).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        future.channel().close();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

package com.yr.nitty.NittyFile3.Chilent2.Chilent;

import com.yr.nitty.NittyFile3.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;

public class EvenChilent extends ChannelInboundHandlerAdapter {
    private String path = "D:\\学习文档\\zj\\cours";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        File file = new File(path);
        fuck(file,ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void fuck(File file, ChannelHandlerContext ctx) {
        try {
            File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
            for (File files : fileArray) {
                if(files.isDirectory()){
                    System.out.println("文件夹:" + files);
                    String paths = files.getPath().replace(path,"");//获得相对路径加文件名
                    Message message = new Message();
                    message.setFileDirectoryLength(paths.getBytes().length);
                    message.setFileDirectory(paths);
                    ctx.writeAndFlush(message);
                    fuck(files,ctx);
                }else {
                    System.out.println("文件:" + files);
                    String paths = files.getPath().replace(path,"");//获得相对路径加文件名
                    Message message = new Message();
                    message.setNameLength(paths.getBytes().length);
                    message.setName(paths);
                    message.setContentLength(files.length());
                    ctx.writeAndFlush(message);

                    if(files.length() > 0 ){
                        RandomAccessFile  raf = null;
                        try {
                            raf = new RandomAccessFile(files.getPath(), "r");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    //future.channel().close();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

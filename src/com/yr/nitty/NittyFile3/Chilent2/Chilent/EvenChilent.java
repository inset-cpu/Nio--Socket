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
    private String path = "D:\\ѧϰ�ĵ�\\zj\\cours";
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
            File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
            for (File files : fileArray) {
                if(files.isDirectory()){
                    System.out.println("�ļ���:" + files);
                    String paths = files.getPath().replace(path,"");//������·�����ļ���
                    Message message = new Message();
                    message.setFileDirectoryLength(paths.getBytes().length);
                    message.setFileDirectory(paths);
                    ctx.writeAndFlush(message);
                    fuck(files,ctx);
                }else {
                    System.out.println("�ļ�:" + files);
                    String paths = files.getPath().replace(path,"");//������·�����ļ���
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

package com.yr.nitty.NittyFile2;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class EvenServer extends ChannelInboundHandlerAdapter {
    private BufferedOutputStream bufferedOutputStream;
    private FileOutputStream out;
    private String pathf="C:\\Users\\19166\\Desktop\\a";
    private long fileLength = 0;
    private long sum= 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof Message){
            Message  ma =(Message)msg;
            if(ma.getMark()==0){
                File file = new File(pathf+ma.getFilePath());
                if(!file.exists()){
                    file.mkdirs();
                }
                ctx.writeAndFlush("b");
                ctx.close();
                return;
            }else {
                File file = new File(pathf+ma.getFilePath());
                if(!file.exists()){
                 file.createNewFile();
                }
                String test = "a";
                fileLength = ma.getFileLength();
                if(fileLength == 0 ){
                    test ="b";
                    ctx.writeAndFlush(test);
                    ctx.close();
                    return ;
                }
                ctx.writeAndFlush(test);
                if(fileLength != 0){
                    out = new FileOutputStream(file);
                    bufferedOutputStream = new BufferedOutputStream(out);
                }
                return;
            }
        }else {
            byte[] bytes = (byte[])msg;
            sum = sum+bytes.length;
            bufferedOutputStream.write(bytes,0, bytes.length);
            bufferedOutputStream.flush();
            if(sum == fileLength){
                bufferedOutputStream.close();
                ctx.writeAndFlush("b");
                ctx.close();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

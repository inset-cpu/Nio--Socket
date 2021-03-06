package com.yr.nitty.NittyFile3.Server;

import com.yr.nitty.NittyFile3.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class EvenServer extends ChannelInboundHandlerAdapter {
    private FileOutputStream out;
    private long contentLength = 0;
    private long sumlength = 0;//获得内容的字节数
    private BufferedOutputStream bufferedOutputStream;
    private static String path = "C:\\Users\\19166\\Desktop\\a\\";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Message){
            Message message = (Message)msg;
            if(message.getFileDirectory() != null){
                File file = new File(path + message.getFileDirectory());
                if (!file.exists()) {
                    file.mkdirs();//创建文件夹
                }
            }else{
                sumlength = message.getContentLength();
                File file = new File(path + message.getName());
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(message.getContentLength() > 0){
                    try {
                         out =  new FileOutputStream(file);
                        bufferedOutputStream = new BufferedOutputStream(out);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }
            return;
        }
        try {
            byte[] bytes= (byte[]) msg;
            contentLength = contentLength + bytes.length;
            bufferedOutputStream.write(bytes, 0, bytes.length);
            bufferedOutputStream.flush();
            if(contentLength == sumlength)
            {
                contentLength = 0 ;
                sumlength = 0;
                bufferedOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        super.exceptionCaught(ctx, cause);
    }
}

package com.yr.nitty.nitty4.Server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class EvenServer extends ChannelInboundHandlerAdapter {
    private boolean fuck = true;
    private   String path = "C:\\Users\\19166\\Desktop\\a\\";
    private   String subfix = ".iso";
    private String Ok ="ojbk";
    private BufferedOutputStream bout;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println(msg);
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        if(fuck){
            String fileName = new String(bytes);
            System.err.println(fileName);
            fuck=false;
            File file = new File(path + fileName + subfix);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try{
                FileOutputStream out =  new FileOutputStream(file);
                bout = new BufferedOutputStream(out);

            }catch (Exception e) {
            e.printStackTrace();
            }
            ctx.writeAndFlush(Ok.getBytes());
            byteBuf.release();
            return;
        }
        try {
            bout.write(bytes, 0, bytes.length);
            byteBuf.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        bout.flush();
        bout.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
    }
}

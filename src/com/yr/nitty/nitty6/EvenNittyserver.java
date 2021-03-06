package com.yr.nitty.nitty6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class EvenNittyserver extends ChannelInboundHandlerAdapter{
    private int counter;

    /**
     * *此方法会在连接到服务器后被调用
     * @param ctx
     * @throws Exception
     */



    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("123");
        String buf = (String)msg;
        System.out.println("**************** :" + buf+"; ======"+ ++counter);
                                              //equalsIgnoreCase不区分大小写比较
//        String currentTime = "QUERY".equalsIgnoreCase(buf) ? new Date(System.currentTimeMillis()).toString() :   " BAD ORDER";
//        currentTime = currentTime+ " ";
//        ByteBuf byteBuf = Unpooled.copiedBuffer(currentTime.getBytes());
//        ctx.writeAndFlush(byteBuf);
//        byte[] req =  ("bbb").getBytes();
//        ByteBuf message = null;
//        message = Unpooled.buffer(30);//创建ByteBuf
//        message.writeBytes(req);
//        ctx.writeAndFlush(message);

    }


    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

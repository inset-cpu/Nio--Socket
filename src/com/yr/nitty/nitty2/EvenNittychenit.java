package com.yr.nitty.nitty2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EvenNittychenit extends ChannelInboundHandlerAdapter {
    private  byte[] req = null;
    private int counter;

    public EvenNittychenit(){
        req = ("QUERY TIME ORDER" +System.getProperty("line.separator")).getBytes();
    }
    /**
     * *此方法会在连接到服务器后被调用
     * @param context
     */
    @Override
    public void channelActive(ChannelHandlerContext context){
        ByteBuf firstMessage = null;
        //输出
        for (int i = 0; i < 100; i++) {
            firstMessage= Unpooled.buffer(req.length);
            firstMessage.writeBytes(req);
            context.writeAndFlush(firstMessage);
        }
        System.out.println("服务端连接成功");
    }

    /**
     * 此方法会在接收到服务器数据后调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("kehuduan : " + body +" ; the counter is :" + ++counter);
    }

    /**
     * *捕捉到异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

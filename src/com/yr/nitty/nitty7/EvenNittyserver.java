package com.yr.nitty.nitty7;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class EvenNittyserver extends ChannelInboundHandlerAdapter{
    private int counter;
    /**
     * *此方法会在连接到服务器后被调用
     * @param ctx
     * @throws Exception
     */
    // 当收到对方发来的数据后，就会触发，参数msg就是发来的信息，可以是基础类型，也可以是序列化的复杂对象。
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("server得到的msg  :" + body+"; ======"+ ++counter);
        ctx.writeAndFlush("hello client !!");
    }
    // channelRead执行后触发
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

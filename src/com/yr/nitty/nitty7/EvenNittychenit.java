package com.yr.nitty.nitty7;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EvenNittychenit extends ChannelInboundHandlerAdapter {
    private int counter;
    /**
     * *此方法会在连接到服务器后被调用
     * @param context
     */
    @Override
    // 通道激活时触发，当客户端connect成功后，服务端就会接收到这个事件，从而可以把客户端的Channel记录下来，供后面复用
    public void channelActive(ChannelHandlerContext context){
        System.out.println("服务端连接成功");
        for (int i = 0; i < 100; i++) {
            context.writeAndFlush("hello server !!");
        }
    }
    /**
     * 此方法会在接收到服务器数据后调用
     * @param ctx
     * @param msg
     * 当收到对方发来的数据后，就会触发，参数msg就是发来的信息，可以是基础类型，也可以是序列化的复杂对象。
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = (String)msg;

        System.out.println("client得到的msg: : " + buf +"; ======"+ ++counter);
    }
    /**
     * *捕捉到异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    // 出错是会触发，做一些错误处理
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        ctx.close();
    }
}

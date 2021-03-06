package com.yr.nitty.nitty1;

import com.sun.org.apache.bcel.internal.classfile.Unknown;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EvenNttyChilent extends SimpleChannelInboundHandler<ByteBuf> {
    
    /**
     *此方法会在连接成功时调用
     */
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        System.out.println("客户端连接服务端成功");
        try {
            Thread.sleep(1 * 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        //channelHandlerContext.write(Unpooled.copiedBuffer("我是客户端 往服务端发消息", CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("我是客户端，往服务端发送消息!", CharsetUtil.UTF_8));
    }

    /**
     * 此方法会在接收服务端消息时调用
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("接到服务端的值: " + ByteBufUtil.hexDump(byteBuf.readBytes(byteBuf.readableBytes())));
    }
    /**
     *捕捉到异常
     * */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

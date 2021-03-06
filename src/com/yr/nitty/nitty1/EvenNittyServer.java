package com.yr.nitty.nitty1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

public class EvenNittyServer extends ChannelInboundHandlerAdapter {
    int counter = 0;
    /**
     * 连接成功调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("我是服务端连接成功");
    }
    /**
     * 读数据
     * @param ctx
     * @param msg
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //2.Object msg->String str
        //readableBytes()长度，readIndex()起始位置
        ByteBuf buf =(ByteBuf)msg;
        byte[] bytes=new byte[buf.readableBytes()];//
        buf.getBytes(buf.readerIndex(),bytes);
        String str=new String(bytes);
        System.out.println("我的服务端，接到的客户端的值  :"+str);


        ctx.writeAndFlush(msg);// 写回数据，
    }
}

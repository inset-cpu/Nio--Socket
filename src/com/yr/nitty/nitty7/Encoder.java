package com.yr.nitty.nitty7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 */
public class Encoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx,  String str, ByteBuf out) throws Exception {
        try{
            // 将对象转换为byte，伪代码，具体用什么进行序列化，你们自行选择。此处用的是fastJson
            byte[] bytes = str.getBytes();
            int dataLength = bytes.length; // 读取消息的长度
            out.writeInt(dataLength); // 先将消息长度写入，也就是消息头
            out.writeBytes(bytes); // 消息体中包含我们要发送的数据
        } catch (Exception e) {
        System.out.println(e + "");
    }
    }
}

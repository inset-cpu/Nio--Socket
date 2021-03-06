package com.yr.nitty.NittyFile3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        if(message.getFileDirectory() == null){
            out.writeInt(1);
            out.writeInt(message.getNameLength());
            out.writeBytes(message.getName().getBytes());
            out.writeLong(message.getContentLength());
        }else{//否则就是文件夹
            out.writeInt(0);
            out.writeInt(message.getFileDirectoryLength());
            out.writeBytes(message.getFileDirectory().getBytes());
        }
    }
}

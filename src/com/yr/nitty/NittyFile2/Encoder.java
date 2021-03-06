package com.yr.nitty.NittyFile2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        if(message.getMark() == 0){
            out.writeInt(0);
            out.writeInt(message.getFilePathLength());
            out.writeBytes(message.getFilePath().getBytes());
        }else{
            out.writeInt(1);
            out.writeInt(message.getFilePathLength());
            out.writeBytes(message.getFilePath().getBytes());
            out.writeLong(message.getFileLength());
        }
    }
}

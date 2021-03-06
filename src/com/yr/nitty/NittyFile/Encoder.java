package com.yr.nitty.NittyFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Msg> {

    protected void encode(ChannelHandlerContext ctx, Msg msg , ByteBuf out) throws Exception {
        if(msg.getState() != null){//发送文件内容
            out.writeInt(2);//表示发送完成
            out.writeInt(msg.getState());
        }else {
            if (msg.getFilepath() != null) {//表示文件目录
                out.writeInt(0);//0表示文件夹
                out.writeInt(msg.getFilepathLength());
                out.writeBytes(msg.getFilepath().getBytes());
            } else {
                out.writeInt(1);//1表示文件
                out.writeInt(msg.getFileNameLength());//发送文件名字节长度
                out.writeLong(msg.getFileLength());//文件内容字节
                out.writeBytes(msg.getFileName().getBytes());//发送文件名
            }
        }
    }
}


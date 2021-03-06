package com.yr.nitty.NittyFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Msg> {

    protected void encode(ChannelHandlerContext ctx, Msg msg , ByteBuf out) throws Exception {
        if(msg.getState() != null){//�����ļ�����
            out.writeInt(2);//��ʾ�������
            out.writeInt(msg.getState());
        }else {
            if (msg.getFilepath() != null) {//��ʾ�ļ�Ŀ¼
                out.writeInt(0);//0��ʾ�ļ���
                out.writeInt(msg.getFilepathLength());
                out.writeBytes(msg.getFilepath().getBytes());
            } else {
                out.writeInt(1);//1��ʾ�ļ�
                out.writeInt(msg.getFileNameLength());//�����ļ����ֽڳ���
                out.writeLong(msg.getFileLength());//�ļ������ֽ�
                out.writeBytes(msg.getFileName().getBytes());//�����ļ���
            }
        }
    }
}


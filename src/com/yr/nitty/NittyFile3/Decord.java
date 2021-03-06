package com.yr.nitty.NittyFile3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decord extends ByteToMessageDecoder {
    private long contentSumLength = 0;
    private long contentLength = 0;
    public static Map<ChannelHandlerContext,Boolean> make = new HashMap<>();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(make.get(ctx) == null ? true : make.get(ctx)){
            in.markReaderIndex(); // ���Ǳ��һ�µ�ǰ��readIndex��λ��					// ��readInt()������������readIndex����4
            int data = in.readableBytes();
            if (data < 4) {
                in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                return;
            }
            int suv = in.readInt();
            if(suv == 0){
                try {
                    data = in.readableBytes();
                    if (data < 4) {
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }
                    int dataLength = in.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf
                    if (dataLength < 0) { // ���Ƕ�����intΪ�������ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }
                    if (in.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }
                    byte[] body = new byte[dataLength];
                    in.readBytes(body);
                    Message message = new Message();
                    message.setFileDirectory(new String(body));
                    System.out.println(new String(body));
                    //mark.put(ctx, false);
                    out.add(message);
                    //mark.put(ctx, true);
                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println("�׳����쳣---Decord");
                }
            }else {
                try {
                    data = in.readableBytes();
                    if (data < 4) { // ���HEAD_LENGTH���������ڱ�ʾͷ���ȵ��ֽ�����
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }

                    int dataLength = in.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf
                    if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�\
                        return ;
                    }

                    if (in.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }

                    byte[] body = new byte[dataLength];
                    in.readBytes(body);

                    if (in.readableBytes() < 8) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
                        in.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
                        return;
                    }

                    long contentLength = in.readLong();
                    Message message = new Message();
                    message.setName(new String(body));
                    System.out.println(new String(body));

                    message.setContentLength(contentLength);

                    contentSumLength = contentLength;

                    make.put(ctx, false);
                    out.add(message);


                    if(contentLength == 0)
                    {
                        make.put(ctx, true);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println("�׳����쳣---Decord");
                }
            }
        }else {
            //��������ܳ��ȼ�ȥ�ۼӵĳ���,���С�ڿɶ�����(�ɶ�����������һ�������������ļ�.�������ʱ��,ֻ����ǰ����ļ��Լ��ĳ���)

            //2000   1000
            if(in.readableBytes() > contentSumLength - contentLength){
                //buf.skipBytes(buf.readableBytes());
                byte[] bytes = new byte[Integer.valueOf(String.valueOf(contentSumLength - contentLength))];
                in.readBytes(bytes);
                contentSumLength = 0;
                contentLength = 0;
                out.add(bytes);
                make.put(ctx, true);
            }else{//����ļ����ܳ���,�������ǿɶ����ۼӵĳ���

                byte[] bytes = new byte[in.readableBytes()];
                contentLength = contentLength + bytes.length;
                in.readBytes(bytes);
                if(contentLength == contentSumLength){
                    contentLength = 0;
                    contentSumLength = 0;
                    make.put(ctx, true);
                }
                out.add(bytes);
            }
        }
    }
}

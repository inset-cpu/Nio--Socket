package com.yr.nitty.NittyFile2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decord extends ByteToMessageDecoder {
    public static Map<ChannelHandlerContext,Boolean> make = new HashMap<>();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(make.get(ctx) == null ? true : make.get(ctx)){
            in.markReaderIndex();
            int datas = in.readableBytes();
            if (datas < 4) {
                in.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
                return;
            }
            int sud = in.readInt();
            if(sud == 0){
                try {
                    int data = in.readableBytes();
                    if(data < 4){
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    int filePathLength = in.readInt(); // 读取传送过来的消息的长度。ByteBuf
                    if (filePathLength < 0) { // 我们读到的int为负数，是不应该出现的情况，这里出现这情况，关闭连接。
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    if (in.readableBytes() < filePathLength) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
                        // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    byte[] body = new byte[filePathLength];
                    in.readBytes(body);
                    make.put(ctx, false);

                    Message message = new Message();
                    message.setMark(sud);
                    message.setFilePath(new String(body));
                    out.add(message);
                }catch (Exception e) {
                    System.err.println("抛出了异常---Decord");
                }
            }else {
                try {
                    int data = in.readableBytes();
                    if (data < 4) {
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }

                    int filePathLength = in.readInt(); // 读取传送过来的消息的长度。ByteBuf
                    if (filePathLength < 0) { // 我们读到的int为负数，是不应该出现的情况，这里出现这情况，关闭连接。
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }

                    if (in.readableBytes() < filePathLength) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
                        // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    data = in.readableBytes();
                    if (data < 8) {
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    if (in.readableBytes() < 8) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
                        // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
                        in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
                        return;
                    }
                    byte[] body = new byte[filePathLength];
                    in.readBytes(body);
                    long fileLength = in.readLong(); // 读取传送过来的消息的长度。ByteBuf
                    make.put(ctx, false);
                    Message message = new Message();
                    message.setMark(sud);
                    message.setFilePath(new String(body));
                    message.setFileLength(fileLength);
                    out.add(message);

                } catch (Exception e) {
                    System.err.println("抛出了异常---Decord");
                }
            }
        }else {
            byte[] bytes = new byte[in.readableBytes()];
            in.readBytes(bytes);
            out.add(bytes);
        }
    }
}

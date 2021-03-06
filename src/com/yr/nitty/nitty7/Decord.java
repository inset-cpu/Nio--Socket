package com.yr.nitty.nitty7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 */
public class Decord extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            in.markReaderIndex(); // 我们标记一下当前的readIndex的位置

            int data = in.readableBytes();
            if(data <  4) {
                return;
            }
            int dataleng = in.readInt(); // 读取传送过来的消息的长度。ByteBuf的readInt()方法会让他的readIndex增加4
            if (dataleng < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
                ctx.close();
            }
            if (in.readableBytes() < dataleng) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
                // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
                in.resetReaderIndex();
                return;
            }
            byte[] body = new byte[dataleng]; // 嗯，这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~~
            in.readBytes(body); //
            String o = new String(body);
            //Object o = JSONObject.parse(body); // 将byte数据转化为我们需要的对象。伪代码，用什么序列化，自行选择。
            out.add(o);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

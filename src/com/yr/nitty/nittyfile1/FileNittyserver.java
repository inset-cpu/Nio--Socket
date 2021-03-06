package com.yr.nitty.nittyfile1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;

public class FileNittyserver extends ChannelInboundHandlerAdapter {
    private int byteRead;//读取的字节数
    //开始下标   volatile:当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
    private volatile int start = 0;
    private String filepath = "C:\\Users\\19166\\Desktop\\a";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * model各个参数详解
     * r 代表以只读方式打开指定文件
     * rw 以读写方式打开指定文件
     * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
     * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
     * 参考网址：https://www.cnblogs.com/nightsu/p/5938950.html
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FileContent){//msg是否属于FileContent实例
            FileContent fileContent = (FileContent)msg;
            byte[] bytes = fileContent.getBytes();//获得fileContent的字节数
            byteRead = fileContent.getEndPos();//获得结束位置
            String fileName = fileContent.getFileName();//文件名
            String path =filepath+ File.separator+fileName;//组成一个新的路径
            File file = new File(path);//构建成一个新的file对象
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");//任意位置访问文件对象
            randomAccessFile.seek(start);//找到开始的下标
            randomAccessFile.write(bytes);//在这个下标开始写入
            start = start + byteRead;//计算出总长度=开始下标+写入字节数
            if (byteRead > 0) {//如果写入的内容数大于0满足条件
                ctx.writeAndFlush(start);//使用管道将字节数write并flush到客户端
            } else {//否则就关闭
                randomAccessFile.close();
                ctx.close();
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
    }
}
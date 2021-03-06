package com.yr.nitty.nittyfile1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileNittychilent extends ChannelInboundHandlerAdapter {
    private RandomAccessFile randomAccessFile;
    private static int count = 0;//发送的次数
    private volatile int start = 0;//开始下标
    private int byteRead;//可读取的字节数
    private FileContent fileContent;//实体类
    private volatile int lastLength = 0;//最后的长度
    public FileNittychilent(FileContent fileContent) {
        if (fileContent.getFile().exists()) {//如果这个文件存在就满足条件
            if (!fileContent.getFile().isFile()) {//判断是否是文件，不是文件满足条件
                System.out.println("Not a file :" + fileContent.getFile());
                return;
            }
        }
        this.fileContent = fileContent;//将这个对象赋给fileUploadFile
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        try {
            randomAccessFile = new RandomAccessFile(fileContent.getFile(), "r");//获得这个文件设置为只读
            randomAccessFile.seek(fileContent.getStartPos());//从指定位置开始
            lastLength = (int) Math.ceil(randomAccessFile.length() / (float) 10);//获得文件内容的总字节数/10
            byte[] bytes = new byte[lastLength];//构建一个长度等于randomAccessFile字节数除以10的byte数组
            if ((byteRead = randomAccessFile.read(bytes)) != -1) {//如果读取的返回值不为-1，也就是有内容，满足条件
                fileContent.setEndPos(byteRead);//设置开始位置为读取的位置
                fileContent.setBytes(bytes);//设置字节数
                ctx.writeAndFlush(fileContent);//通过通道将fileContent对象输出出去
            } else {
                System.out.println("文件已经读完");
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * 读取服务端返回的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof  Integer){//判断是否是integer类型
            start = (Integer)msg;
            System.out.println("第"+ ++count +"次发送");
            System.out.println("已读取长度:"+start);
            if(start != -1){
                randomAccessFile =new RandomAccessFile(fileContent.getFile(),"r");
                randomAccessFile.seek(start);
                System.out.println("块儿长度：" + (int)Math.ceil(randomAccessFile.length() / (float)10));//打印快长
                System.out.println("剩余长度：" + (randomAccessFile.length() - start));//打印还剩多少长度
                int a = (int)(randomAccessFile.length() - start);//获得剩余的长度
                int b = (int)Math.ceil(randomAccessFile.length() / (float)10);//获得平均每次发送的长度
                if(a < b ) {//如果b大于a表示，最后一次发送
                    lastLength  = a ;//将剩余的长度赋给lastLength变量，也就是除以10的余数
                }
                byte[] bytes = new byte[lastLength];
                if((byteRead = randomAccessFile.read(bytes)) != 1 && (randomAccessFile.length() - start) > 0){//读取到byte数组里有值并且读取的字节不为负数满足条件
                    System.out.println("本次读取的长度：" + bytes.length);//打印byte长度
                    fileContent.setEndPos(byteRead);//设置结束的偏移量等于读取的字节数
                    fileContent.setBytes(bytes);//将bytes设置到fileUploadFile对象中
                    try {
                        ctx.writeAndFlush(fileContent);//再次将这个对象传入服务端
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {//已经没有内容了，关闭流
                    randomAccessFile.close();
                    ctx.close();
                    System.out.println("文件已经读完--------" + byteRead);
                }
            }
        }
    }

    /**
     * 抛异常进入
     * @param ctx
     * @param cause
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause);
        cause.printStackTrace();
        ctx.close();
    }
}

package com.yr.nitty.NittyFile.Chilent;

import com.yr.nitty.NittyFile.Msg;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class EnvChenilt extends ChannelInboundHandlerAdapter {

    private String path ="";
    private File file;
    private List<File> list = new ArrayList<>();//存放创建的空文件集合
    private int i = 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        fuck(new File(path), ctx);//发送文件对象
        System.out.println("发送文件数:"+i);
        Msg msg = new Msg();
        msg.setState(1);//表示发送完成
        ctx.writeAndFlush(msg);//循环完毕将发送文件数传递过去
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        for (File file:list) {
            System.out.println(file);
            //发送内容
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "r");//根据指定路径获取只读的对象
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //发送字节
                ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        //future.channel().close();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fuck(File file, ChannelHandlerContext ctx){
        try {
            File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
            for (File files:fileArray) {//循环文件下的所有文件
                ++i;
                if (files.isDirectory()) {//是否是文件夹
                    System.out.println("文件夹:" + files);
                    Msg msg = new Msg();
                    String pathName = files.getPath().replace(path,"");//获得相对路径加文件名
                    msg.setFilepathLength(pathName.getBytes().length);
                    msg.setFilepath(pathName);//将文件目录路径加入

                    ctx.writeAndFlush(msg);//将对象通过通道输出
                    fuck(files, ctx);
                } else {//否则是文件
                    this.file = files;//将files对象赋给file对象
                    System.out.println("文件:" + files);
                    //发送除文件内容外所有信息
                    Msg msg = new Msg();
                    String filename = files.getPath().replace(path,"");//获得相对路径加文件名
                    msg.setFileNameLength(filename.getBytes().length);
                    msg.setFileName(filename);
                    msg.setFileLength(files.length());//设置内容长度的字节
                    ctx.writeAndFlush(msg);//将对象通过通道输出

                    if(msg.getFileLength() != 0){
                        //a.txt
                        //b.txt
                        list.add(files);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

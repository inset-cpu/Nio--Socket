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
    private List<File> list = new ArrayList<>();//��Ŵ����Ŀ��ļ�����
    private int i = 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        fuck(new File(path), ctx);//�����ļ�����
        System.out.println("�����ļ���:"+i);
        Msg msg = new Msg();
        msg.setState(1);//��ʾ�������
        ctx.writeAndFlush(msg);//ѭ����Ͻ������ļ������ݹ�ȥ
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        for (File file:list) {
            System.out.println(file);
            //��������
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "r");//����ָ��·����ȡֻ���Ķ���
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //�����ֽ�
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
            File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
            for (File files:fileArray) {//ѭ���ļ��µ������ļ�
                ++i;
                if (files.isDirectory()) {//�Ƿ����ļ���
                    System.out.println("�ļ���:" + files);
                    Msg msg = new Msg();
                    String pathName = files.getPath().replace(path,"");//������·�����ļ���
                    msg.setFilepathLength(pathName.getBytes().length);
                    msg.setFilepath(pathName);//���ļ�Ŀ¼·������

                    ctx.writeAndFlush(msg);//������ͨ��ͨ�����
                    fuck(files, ctx);
                } else {//�������ļ�
                    this.file = files;//��files���󸳸�file����
                    System.out.println("�ļ�:" + files);
                    //���ͳ��ļ�������������Ϣ
                    Msg msg = new Msg();
                    String filename = files.getPath().replace(path,"");//������·�����ļ���
                    msg.setFileNameLength(filename.getBytes().length);
                    msg.setFileName(filename);
                    msg.setFileLength(files.length());//�������ݳ��ȵ��ֽ�
                    ctx.writeAndFlush(msg);//������ͨ��ͨ�����

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

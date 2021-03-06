package com.yr.nio.zuoye.McMf;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class FileChile {
    private static String path ="";
    private static SocketChannel socketChannel = null;
    private static SocketAddress socketAddress = null;

    public static void main(String[] args) throws  Exception{
        socketChannel = SocketChannel.open();
        socketAddress = new InetSocketAddress(InetAddress.getLocalHost(),2223);
        socketChannel.socket().setReuseAddress(true);
        socketChannel.connect(socketAddress);

        FileChile.filepath(path);
        Thread.sleep(100*1000);
    }
    public static void  filepath(String paths) throws Exception {
        File file = new File(paths);
        if(file.isDirectory()){//�Ƿ����ļ���
            if(!paths.equals(path)){//�Ƿ��ǳ�ʼ·��
                //���ͱ�ʶ
                ByteBuffer buffer1 =ByteBuffer.allocate(4);
                buffer1.putInt(1);
                buffer1.flip();
                socketChannel.write(buffer1);
                buffer1.clear();

                String filepaths = paths.replace(path+"\\","");
                //�����ļ�·������
                ByteBuffer buffer2 = ByteBuffer.allocate(4);
                buffer2.putInt(new String(filepaths.getBytes(),"ISO-8859-1").length());
                buffer2.flip();//ˢ��
                socketChannel.write(buffer2);//����
                buffer2.clear();
                //�����ļ�·��
                ByteBuffer buffer3 = ByteBuffer.allocate(new String(filepaths.getBytes(),"ISO-8859-1").length());
                buffer3.put(filepaths.getBytes(StandardCharsets.UTF_8));// StandardCharsets.UTF_8  jdk�Դ����ַ����볣���ֶ�
                buffer3.flip();
                socketChannel.write(buffer3);
                buffer3.clear();
            }
            String files[] = file.list();
            for (String filelist : files){
                filepath(paths+File.separator+filelist);
            }
        }else {
            String filepaths = paths.replace(path+"\\","");//�滻����Ŀ¼
            //���ͱ�ʶ
            ByteBuffer buffer4 =ByteBuffer.allocate(4);
            buffer4.putInt(2);
            buffer4.flip();
            socketChannel.write(buffer4);
            buffer4.clear();
            //�����ļ�������
            ByteBuffer buffer5 =ByteBuffer.allocate(4);
            buffer5.putInt(new String(filepaths.getBytes(),"ISO-8859-1").length());
            buffer5.flip();
            socketChannel.write(buffer5);
            buffer5.clear();
            //�����ļ���
            ByteBuffer buffer6 = ByteBuffer.allocate(new String(filepaths.getBytes(),"ISO-8859-1").length());
            buffer6.put(filepaths.getBytes(StandardCharsets.UTF_8));// StandardCharsets.UTF_8  jdk�Դ����ַ����볣���ֶ�
            buffer6.flip();
            socketChannel.write(buffer6);
            buffer6.clear();
            //�����ļ�����
            ByteBuffer buffer7 =ByteBuffer.allocate(8);
            Long  fileleng = new File(paths).length();
            buffer7.putLong(fileleng);
            buffer7.flip();
            socketChannel.write(buffer7);
            buffer7.clear();
            //�����ļ�
            ByteBuffer buffer8 = ByteBuffer.allocate(1024*1024);
            FileInputStream fileInputStream = new FileInputStream(new File(paths));

        }
    }
}

package com.yr.nio.zuoye.manychilemanyfile;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;


public class NioCilent1 {
    private static String path ="C:\\Users\\19166\\Desktop\\ר����";
    private static SocketChannel socketChannel =null;
    private static SocketAddress socketAddress = null;

    public static void main(String[] args) throws Exception {
        socketChannel = SocketChannel.open();
        socketAddress = new InetSocketAddress(InetAddress.getLocalHost(),8888);
        socketChannel.socket().setReuseAddress(true);
        socketChannel.connect(socketAddress);

        NioCilent1.sePath(path);
        Thread.sleep(100*1000);
    }
    public static  void sePath(String paths) throws Exception {
        File file = new File(paths);
        if (file.isDirectory()){
            if (!paths.equals(path)){
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.putInt(1);//��ʾ���ļ�
                buffer.flip();//ˢ��
                socketChannel.write(buffer);
                buffer.clear();

                String rfilepath = paths.replace(path + "\\", "");// �滻����·��

                ByteBuffer buffer0 = ByteBuffer.allocate(4);
                buffer0.putInt(new String(rfilepath.getBytes(), "ISO-8859-1").length());// ����·������
                buffer0.flip();//ˢ��
                socketChannel.write(buffer0);// ����·��
                buffer0.clear();

                ByteBuffer buffer2 = ByteBuffer.allocate(new String(rfilepath.getBytes(), "ISO-8859-1").length());
                buffer2.put(rfilepath.getBytes());
                buffer2.flip();//ˢ��
                socketChannel.write(buffer2);// ����·��
                buffer2.clear();
            }

            String files []= file.list();
            for (String fPath : files) {
                sePath(paths+File.separator + fPath);
                System.out.println(paths+File.separator + fPath);
            }
        }else{

            ByteBuffer buffer0 = ByteBuffer.allocate(4);
            buffer0.putInt(2);//��ʾ���ļ�
            buffer0.flip();//ˢ��
            socketChannel.write(buffer0);
            buffer0.clear();

            String rfilepath = paths.replace(path + "\\", "");

            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            buffer1.putInt(new String (rfilepath.getBytes(),"ISO-8859-1").length());
            buffer1.flip();//ˢ��
            socketChannel.write(buffer1);
            buffer1.clear();

            ByteBuffer buffer2 = ByteBuffer.allocate(new String (rfilepath.getBytes(),"ISO-8859-1").length());
            buffer2.put(rfilepath.getBytes());
            buffer2.flip();
            socketChannel.write(buffer2);
            buffer2.clear();

            ByteBuffer buffer3 = ByteBuffer.allocate(8);
            long filepath = new File(paths).length();
            buffer3.putLong(filepath);
            buffer3.flip();
            socketChannel.write(buffer3);
            buffer3.clear();

            ByteBuffer buffer4 = ByteBuffer.allocate(1024*1024*20);
            FileInputStream fileInputStream =new FileInputStream(new File(paths));
            FileChannel fileChannel = fileInputStream.getChannel();
            long now = 0;// ÿ�ζ�ȡ���ļ����ݳ���
            long sum = 0;// �ۼӳ���
            do {
                now = fileChannel.read(buffer4);
                sum += now;
                buffer4.flip();
                socketChannel.write(buffer4);
                buffer4.clear();
            } while (now != -1 && sum < filepath);
        }
    }
}


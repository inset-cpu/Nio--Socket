package com.yr.nio.zuoye.onechiletmanyfile;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NioCilent {
    private static String path ="C:\\Users\\19166\\Desktop\\head";
    private static SocketChannel socketChannel;
    private static SocketAddress socketAddress;

    public static void main(String[] args) throws Exception {
        socketChannel = SocketChannel.open();
        socketAddress = new InetSocketAddress(InetAddress.getLocalHost(),8888);
        socketChannel.connect(socketAddress);
        NioCilent.sePath(path);
        Thread.sleep(100*10000);
    }
    public static  void sePath(String paths) throws Exception {
        File file = new File(paths);
        if (file.isDirectory()){
            if (!paths.equals(path)){
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.putInt(1);//表示是文件
                buffer.flip();//刷新
                socketChannel.write(buffer);
                buffer.clear();

                ByteBuffer buffer0 = ByteBuffer.allocate(4);
                String rfilepath = paths.replace(path + "\\", "");// 替换基础路径
                buffer0.putInt(new String(rfilepath.getBytes(), "ISO-8859-1").length());// 发送路径长度
                buffer0.put(paths.getBytes());
                buffer0.flip();//刷新
                socketChannel.write(buffer0);// 发送路径
                buffer0.clear();
            }
            String files []= file.list();
            for (String fPath : files) {
                sePath(paths+File.separator + fPath);
            }
        }else{
            String rfilepath = paths.replace(path + "\\", "");
            ByteBuffer buffer0 = ByteBuffer.allocate(4);
            buffer0.putInt(2);//表示是文件
            buffer0.flip();//刷新
            socketChannel.write(buffer0);
            buffer0.clear();

            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            buffer1.putInt(new String (rfilepath.getBytes(),"ISO-8859-1").length());
            buffer1.flip();//刷新
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
            long now = 0;// 每次读取的文件内容长度
            long sum = 0;// 累加长度
            do {
                now = fileChannel.read(buffer4);
                sum += now;
                buffer4.flip();
                socketChannel.write(buffer4);
                buffer4.clear();
            } while (now != -1 && sum < filepath);
            String a ="发送成功";
            System.out.println(file.getName()+a);
        }
    }
}

package com.yr.nio.zuoye.file2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NioCilent2 {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress("192.168.1.122",8859));
        File file = new File("C:\\Users\\19166\\Desktop\\A.jsp");
        String fileName = file.getName();

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(fileName.getBytes().length);
        buffer.flip();//刷新
        socketChannel.write(buffer);
        buffer.clear();

        buffer = ByteBuffer.allocate(fileName.getBytes().length);
        buffer.put(fileName.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();

        buffer  = ByteBuffer.allocate(8);
        buffer.putLong(file.length());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();

        FileChannel fileChannel = new FileInputStream(file).getChannel();
         buffer = ByteBuffer.allocate(1024*1024);
       while (fileChannel.read(buffer) > 0){
           buffer.flip();
           socketChannel.write(buffer);
           buffer.clear();
       }
       Thread.sleep(10*1000);
    }
    //将文件转换成byte
    private static byte[] makeFileToByte(String fileFath) throws IOException {
        File file = new File(fileFath);
        FileInputStream fis = new FileInputStream(file);
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        int temp = 0;
        int index = 0;
        while(true){
            index = fis.read(bytes,temp,length - temp);
            if(index <= 0 )
                break;
            temp += index;
        }
        fis.close();
        return bytes;
    }
}

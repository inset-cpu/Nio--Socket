package com.yr.nio.zuoye.manychileonefile;



import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class CilentThread1 extends  Thread{
    private String filepath =  null;
    private String paths = null;
    public CilentThread1(String filepath, String paths) {
        this.filepath=filepath;
        this.paths =paths;
    }
    @Override
    public void run() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 6666);

            socketChannel.socket().setReuseAddress(true);//ʹ�÷���ص�����ʱ�����ʹ�øö˿ڣ���������ʾ�˿�ռ�á�
            socketChannel.connect(socketAddress);

            cilentfile(socketChannel,filepath,paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cilentfile(SocketChannel socketChannel, String filepath, String paths) throws  Exception{
        String files = filepath.replace(paths + "\\" , "");
        //���ͱ�ʶ
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(2);
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
        //�����ļ�����
        ByteBuffer buffer1 = ByteBuffer.allocate(4);
        buffer1.putInt(new String(files.getBytes(),"ISO-8859-1").length());
        buffer1.flip();
        socketChannel.write(buffer1);
        buffer1.clear();
        //�����ļ���
        ByteBuffer buffer2 = ByteBuffer.allocate(new String(files.getBytes(),"ISO-8859-1").length());
        buffer2.put(files.getBytes());
        buffer2.flip();
        socketChannel.write(buffer2);
        buffer2.clear();
        //�����ļ�����
        ByteBuffer buffer3 = ByteBuffer.allocate(8);
        long filelong =  new File(filepath).length();
        buffer3.putLong(filelong);
        buffer3.flip();
        socketChannel.write(buffer3);
        buffer3.clear();
        //�����ļ�
        ByteBuffer buffer4 = ByteBuffer.allocate(1024*1024);
        FileInputStream fileInputStream = new FileInputStream(new File(filepath));
        FileChannel fileChannel = fileInputStream.getChannel();
        long cumulative = 0;//�ۼӳ���
        long readlength = 0;//ÿ�ζ�ȡ����
        do {
            readlength = fileChannel.read(buffer4);
            cumulative += readlength;
            buffer4.flip();
            socketChannel.write(buffer4);
            buffer4.clear();
        }while (readlength != -1 && cumulative < filelong);

        fileChannel.close();
        fileInputStream.close();

        Niocilent.a--;
        socketChannel.socket().close();
        socketChannel.close();
    }
}

package com.yr.nio.zuoye.manychileonefile;



import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CilentThread2 extends Thread{
    private String paths = null;
    private String filepath = null;
    public CilentThread2(String paths, String filepath) {
        this.filepath=filepath;
        this.paths=paths;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 6666);

            socketChannel.socket().setReuseAddress(true);
            socketChannel.connect(socketAddress);

            fileName(socketChannel,paths,filepath);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * �����ļ���
     * @param socketChannel
     * @param filepath
     * @param paths
     * @throws Exception
     */
    public  void fileName(SocketChannel socketChannel,String filepath,String paths) throws Exception {
        filepath = filepath.replace(paths+"\\","");
        //���ͱ�ʶ
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(1);
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
        //�����ļ�����
        ByteBuffer buffer1= ByteBuffer.allocate(4);
        buffer1.putInt(new String(filepath.getBytes(),"ISO-8859-1").length());
        buffer1.flip();
        socketChannel.write(buffer1);
        buffer1.clear();
        //�����ļ���
        ByteBuffer buffer2= ByteBuffer.allocate(new String(filepath.getBytes(),"ISO-8859-1").length());
        buffer2.put(filepath.getBytes());
        buffer2.flip();
        socketChannel.write(buffer2);
        buffer2.clear();

        Niocilent.a--;
        socketChannel.socket().shutdownOutput();
    }
}

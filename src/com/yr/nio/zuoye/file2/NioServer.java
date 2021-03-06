package com.yr.nio.zuoye.file2;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioServer {
    public static void main(String[] args) throws Exception {


        //打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建一个选择器
        Selector selector = Selector.open();
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定监听端口
        serverSocket.bind(new InetSocketAddress(8859));
        //注册选择器，保持等待模式
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        FileChannel fileChannel = null;
        boolean make = true;
        while(true){
            selector.select();
            //返回此选择器的已选择键集
            Iterator<SelectionKey> iterKey= selector.selectedKeys().iterator();
            try {
                while(iterKey.hasNext()){
                    SelectionKey key = iterKey.next();
                    if(key.isAcceptable()){
                        ServerSocketChannel serverChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel1.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buf = null;
                        if (make) {
                            int nameLength = 0;
                            byte[] bytes = new byte[4];
                            buf = ByteBuffer.allocate(4);
                            socketChannel.read(buf);
                            buf.flip();
                            nameLength = buf.getInt();
                            buf.clear();

                            String name;
                            bytes = new byte[nameLength];
                            buf = ByteBuffer.allocate(nameLength);
                            socketChannel.read(buf);
                            buf.flip();
                            buf.get(bytes);
                            buf.clear();
                            name = new String(bytes);

                            // 文件长度
                            long contentLength = 0;
                            bytes = new byte[8];
                            buf = ByteBuffer.allocate(8);
                            socketChannel.read(buf);
                            buf.flip();
                            contentLength = buf.getLong();
                            buf.clear();
                            System.out.println(contentLength);

                            // 文件内容
                            File file = new File("C:\\Users\\19166\\Desktop\\a\\" + name);
                            fileChannel = new FileOutputStream(file).getChannel();
                            make = false;
                        }
                        buf = ByteBuffer.allocate(1024 * 1024);
                        while (socketChannel.read(buf) > 0) {
                            buf.flip();
                            fileChannel.write(buf);
                            buf.clear();
                        }
                    }
                    iterKey.remove(); // 删除已经处理过的请求
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
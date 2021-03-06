package com.yr.nio.zuoye.file1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
    private Selector  selector; //通道管理器
    /**
     * 获得一个Socket通道，并对该通道做一些初始化的工作
     * @param ip 连接的服务器的ip
     * @param port  连接的服务器的端口号
     * @throws IOException
     */
    public void head(String ip , int port) throws IOException {
        SocketChannel sc =SocketChannel.open();// 获得一个Socket通道
        sc.configureBlocking(false);// 设置通道为非阻塞
        selector  = Selector.open();// 获得一个通道管理器
        // 客户端连接服务器,其实方法执行并没有实现连接，需要在reads（）方法中调
        //用channel.finishConnect();才能完成连接
        sc.connect(new InetSocketAddress(ip,port));
        sc.register(selector, SelectionKey.OP_CONNECT);///将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
    }
    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    public void reads() throws Exception {
        while (true){// 轮询访问selector
            selector.select();
            Iterator iot =this.selector.selectedKeys().iterator();// 获得selector中选中的项的迭代器
            while (iot.hasNext()){//表示你是否有输入数据
                SelectionKey key = (SelectionKey) iot.next();
                iot.remove();// 删除已选的key,以防重复处理
                if(key.isConnectable()){// // 连接事件发生
                    SocketChannel channel = (SocketChannel) key.channel();
                    if(channel.isConnectionPending()){// 判断此通道上是否正在进行连接操作。// 如果正在连接，则完成连接
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);// 设置成非阻塞
                    for(int i = 0;i<10;i++)
                    {
                        try {
                            Thread.sleep(1 * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        channel.write(ByteBuffer.wrap(new String("向服务端发送了一条信息-----------------" + i).getBytes()));
                    }
                    channel.register(this.selector,SelectionKey.OP_READ);  //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限
                }else if(key.isReadable()){ // 获得了可读的事件
                    read(key);
                }
            }
        }
    }
    public  void read (SelectionKey key) throws Exception {
        System.err.println("key============="+key.hashCode());
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer =  ByteBuffer.allocate(1024);// 创建读取的缓冲区  
        channel.read(buffer);
        byte[] tata=  buffer.array();
        String  mcsg = new String(tata).trim();
        System.out.println(1);
        System.out.println("客户端收到的服务器传来的消息＝＝＝："+mcsg  + "    " + key.hashCode());
        System.out.println(2);
//        ByteBuffer outBuffer = ByteBuffer.wrap(mcsg.getBytes());
//        channel.write(outBuffer);// 将消息回送给服务端
    }
    public static void main(String[] args) throws Exception {
    NIOClient nioClient = new NIOClient();
    nioClient.head("localhost",8880);
    nioClient.reads();
    }
}

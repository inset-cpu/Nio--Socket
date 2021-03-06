package com.yr.nio.zuoye.file1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    private Selector selector; //通道管理器
    public void insocket (int port) throws IOException {
        ServerSocketChannel ser =  ServerSocketChannel.open(); // 获得一个ServerSocket通道
        ser.configureBlocking(false);// 设置通道为非阻塞
        ser.socket().bind(new InetSocketAddress(port));// 将该通道对应的ServerSocket绑定到port端口
        this.selector = Selector.open();// 获得一个通道管理器
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        ser.register(selector, SelectionKey.OP_ACCEPT);
    }
    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    public void haset ()throws Exception {
        System.out.println("成功连接客服端");
        while (true){ // 轮询访问selector
            selector.select();//当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            Iterator iter = this.selector.selectedKeys().iterator();// 获得selector中选中的项的迭代器，选中的项为注册的事件
            while (iter.hasNext()){//表示你是否有输入数据
            SelectionKey selectionKey = (SelectionKey) iter.next();
            iter.remove();// 删除已选的key,以防重复处理
                // 客户端请求连接事件，客户端请求连接，只会进入一次
                if(selectionKey.isAcceptable()){//测试此键的通道是否已完成其套接字连接操作
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel channel =server.accept();// 获得和客户端连接的通道
                    channel.configureBlocking(false);// 设置成非阻塞
                    //在这里可以给客户端发送信息哦
                    channel.write(ByteBuffer.wrap(new String("某客户端已经连接成功").getBytes()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);//注册可读事件
                }else if (selectionKey.isReadable()){// 获得了可读的事件
                    read(selectionKey);
                }
            }
        }
    }
    public void read (SelectionKey key) throws IOException{
        System.out.println("key ============ "+key.hashCode());
        SocketChannel channel = (SocketChannel) key.channel(); // 服务器可读取消息:得到事件发生的Socket通道
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);// 创建读取的缓冲区
        channel.read(byteBuffer);
        byte[] tat =byteBuffer.array();
        String msg = new String(tat).trim();
        System.err.println("服务端收到信息："+msg+ "    " + key.hashCode());
        String SS = "服务端";
        ByteBuffer outbyte = ByteBuffer.wrap(SS.getBytes());
        channel.write(outbyte);
    }
    public static void main(String[] args) throws Exception{
        NIOServer nioServer = new NIOServer();
        nioServer.insocket(8880);
        nioServer.haset();
    }
}

package com.yr.nio.zuoye.manychileonefile;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Nioserver {
    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;
    private ServerThread serverThread;

    public Nioserver(){

    }

    public Nioserver(ServerThread serverThread) throws  Exception{
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.serverThread=serverThread;
        while (true){

            int readyChannelsNum = selector.select(2000);//1.2.3,4,1,2,3,12,123,,123,123,123,
            if (readyChannelsNum == 0) {
                System.out.println("�ȴ�����...");
                continue;
            }
            Iterator<?> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key =(SelectionKey) iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(this.selector, SelectionKey.OP_READ);
                } else if (key.isReadable()){
                    this.serverThread.fuck(key);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws  Exception{
        new Nioserver(new ServerThread());
    }
}

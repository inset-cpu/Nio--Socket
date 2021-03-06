package com.yr.nio.zuoye.manychilemanyfile;

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
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.serverThread=serverThread;
        while (true){
            int rum = selector.select(2000);
            if (rum == 0) {
                System.out.println(111);
                System.out.println("�ȴ�����...");
                continue;
            }
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    System.err.println(1);
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(this.selector, SelectionKey.OP_READ);
                } else if (key.isReadable()){
                    ServerThread.fuck(key);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws  Exception{
        new Nioserver(new ServerThread());
    }
}

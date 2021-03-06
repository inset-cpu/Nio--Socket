package com.yr.nio.zuoye.fire5;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class FileClient {
	//
	private static ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private static SocketChannel socketChannel;
	public static void main(String[] args) {
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			Selector selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			socketChannel.connect(new InetSocketAddress("localhost", 6666));
			while (true) {
				selector.select();
				Set<SelectionKey> keySet = selector.selectedKeys();
				for (final SelectionKey key : keySet) {
					if(key.isConnectable()){
						socketChannel = (SocketChannel)key.channel();
						socketChannel.finishConnect();
						socketChannel.register(selector, SelectionKey.OP_WRITE);
					}else if(key.isWritable()){
						FileInputStream fis = null;
						FileChannel channel = null;
						try {
							fis = new FileInputStream("C:\\Users\\19166\\Desktop\\A.war");
							channel = fis.getChannel();
							int i = 1;
							int count = 0;
							while((count = channel.read(sendBuffer)) != -1) {
								sendBuffer.flip(); 
								int send = socketChannel.write(sendBuffer);
								while(send == 0){
									Thread.sleep(10);
									send = socketChannel.write(sendBuffer);
								}
								sendBuffer.clear(); 
				           }
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								channel.close();
								fis.close();
								socketChannel.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				keySet.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

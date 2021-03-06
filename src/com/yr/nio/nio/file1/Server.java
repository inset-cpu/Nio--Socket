package com.yr.nio.nio.file1;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
	
	
	public static void main(String[] args) throws Exception {

		FileChannel fileChannel = null;
		int sumLength = 0;
		 
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(8888));
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			try {
				selector.select();

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					if (key.isAcceptable()) {
						
						ServerSocketChannel serverChannel1 = (ServerSocketChannel) key.channel();
						SocketChannel socketChannel = serverChannel1.accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ); 
						//����ֻ�����һ��
						
						//����������ļ�������
						File file = new File("C:\\Users\\Acer\\Desktop\\a\\a.zip");
						fileChannel = new FileOutputStream(file).getChannel();  
					} else if (key.isReadable()) {
						
						
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer  revBuffer = ByteBuffer.allocate(1024 * 1024);
						
						while(client.read(revBuffer) > 0){
							revBuffer.flip();
							fileChannel.write(revBuffer);
							revBuffer.clear();
						}
						
						
						//client.write(ByteBuffer.wrap("���Ƿ��������͸��ͻ��˵���Ϣ".getBytes()));
						
					}
					it.remove(); // ɾ���Ѿ������������
				}

			} catch (Exception e) {

			}
		}
	}
}

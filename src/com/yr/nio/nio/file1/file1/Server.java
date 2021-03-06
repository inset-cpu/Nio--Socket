package com.yr.nio.nio.file1.file1;

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
		long sumLength = 0;
		long length = 0;
		boolean mark = true;
		 
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
						if(mark)
						{
							ByteBuffer  revBuffer = ByteBuffer.allocate(8);
							client.read(revBuffer);
							sumLength = revBuffer.getLong();
							mark = false;
						}
						
						
						ByteBuffer  revBuffer = ByteBuffer.allocate(1024 * 1024);
						int a = 0;
						while((a = client.read(revBuffer)) > 0){
							length = length + a;
							revBuffer.flip();
							fileChannel.write(revBuffer);
							revBuffer.clear();
						}
						
						
						if(length == sumLength)
						{
							client.write(ByteBuffer.wrap("over".getBytes()));
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

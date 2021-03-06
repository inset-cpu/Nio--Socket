package com.yr.nio.nio.file2;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {
	
	
	public static void main(String[] args) throws Exception {

		FileChannel fileChannel = null;
		int sumLength = 0;
		boolean mark =true;
		 
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
						 
					} else if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						if(mark)
						{
							System.out.println("11111111111");//200=10000-200
							ByteBuffer  bufferhead = ByteBuffer.allocate(200);
							int a = client.read(bufferhead);
							 
							 String head = new String( bufferhead.array(),0,a);
							 String heads[] = head.split("\\|");
							 System.out.println(heads[0]);
							 
							 File file = new File("C:\\Users\\Acer\\Desktop\\a\\"+heads[0]);
							 fileChannel = new FileOutputStream(file).getChannel();
							 mark = false;
						}
						 
						
						
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

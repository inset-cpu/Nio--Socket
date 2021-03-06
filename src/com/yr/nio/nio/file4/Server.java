package com.yr.nio.nio.file4;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {

	public static void main(String[] args) throws Exception {

		int sumLength = 0;

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(8888));
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		boolean mark = true;
		FileChannel fileChannel = null;
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
					} else if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buf = null;
						if (mark) {
							int nameLength = 0;
							byte[] bytes = new byte[4];
							buf = ByteBuffer.allocate(4);
							client.read(buf);
							buf.flip();
							nameLength = buf.getInt();
							buf.clear();

							String name=null;
							bytes = new byte[nameLength];
							buf = ByteBuffer.allocate(nameLength);
							client.read(buf);
							buf.flip();
							buf.get(bytes);
							buf.clear();
							name = new String(bytes);

							long fileLenght = 0;
							bytes = new byte[8];
							buf = ByteBuffer.allocate(8);
							client.read(buf);
							buf.flip();
							fileLenght = buf.getLong();
							buf.clear();

							File file = new File("C:\\Users\\19166\\Desktop\\a\\" + name);
							fileChannel = new FileOutputStream(file).getChannel();
							mark = false;
						}
						buf = ByteBuffer.allocate(1024 * 1024);
						while (client.read(buf) > 0) {
							System.out.println(1);
							buf.flip();
							fileChannel.write(buf);
							buf.clear();
						}
					}
					it.remove(); // ɾ���Ѿ������������
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

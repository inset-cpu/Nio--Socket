package com.yr.nio.zuoye.fire5;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class FileServer {
	private static ByteBuffer Buffer = ByteBuffer.allocate(1024);
	//�ļ���û���������Զ����ļ�����ֻ�����ļ�����
	public static void main(String[] args) throws Exception {
		FileOutputStream fout = null;
		FileChannel fch = null;

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(6666));
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


		while (true) {
			try {
				selector.select();   // ����ֵΪ���δ������¼���
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				
				for (SelectionKey key : selectionKeys) {
					ServerSocketChannel server = null;
					SocketChannel client = null;
					int count = 0;
					if (key.isAcceptable()) {
						server = (ServerSocketChannel) key.channel();
						System.out.println("�пͻ������ӽ���==)");
						client = server.accept();
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ);
						fout = new FileOutputStream("C:\\Users\\19166\\Desktop\\a\\" + client.hashCode() + ".war");
						fch = fout.getChannel();
					} else if (key.isReadable()) {
						client = (SocketChannel) key.channel();
						Buffer.clear();
						count = client.read(Buffer);
						int k = 0;
						// ѭ����ȡ�����������ݣ�
						while(count > 0){
							System.out.println("k=" + (k++) + " ��ȡ��������:" + count);
							Buffer.flip();
							fch.write(Buffer);
							fout.flush();
							Buffer.clear();
							count = client.read(Buffer);
						}
						if(count == -1){
							client.close();
							fch.close();
							fout.close();
						}
					}
					else if (key.isWritable()) {
						System.out.println("����˽��ճɹ�");
					}
				}
				selectionKeys.clear();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

		}
	}
}

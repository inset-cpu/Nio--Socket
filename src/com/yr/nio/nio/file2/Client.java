package com.yr.nio.nio.file2;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
	public static void main(String[] args) throws Exception {
		// socketChannel �������ӷ���˵Ĺܵ�(������д��)
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));

		File file = new File("I:\\linux\\linuxIso\\rhel-server-7.0-x86_64-dvd.iso");

		ByteBuffer bufferhead = ByteBuffer.allocate(200);
		String head = file.getName() + "|" + file.length() + "|";
		bufferhead.put(head.getBytes(Charset.forName("UTF-8")));
		
		int start = bufferhead.capacity() - bufferhead.position();
		for (int i = 0; i < start; i++) {
			bufferhead.put(";".getBytes(Charset.forName("UTF-8")));
        }
		bufferhead.flip();
		socketChannel.write(bufferhead);
		bufferhead.clear();
		

		FileChannel fileChannel = new FileInputStream(file).getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
		while (fileChannel.read(buffer) > 0) {
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}
		
		Thread.sleep(10 *1000);
//
//		FileChannel fileChannel = new FileInputStream(file).getChannel();
//		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
//
//		int num = 0;
//		while ((num = fileChannel.read(buffer)) > 0) {
//			buffer.flip();
//			socketChannel.write(buffer);
//			buffer.clear();
//		}

		
		
		

	}
}
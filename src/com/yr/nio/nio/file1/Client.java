package com.yr.nio.nio.file1;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {
	public static void main(String[] args) throws Exception {
		// socketChannel �������ӷ���˵Ĺܵ�(������д��)
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));

		File file = new File("I:\\notes\\mysql\\db.zip");
//        �����ļ��ܵ�(�ӱ��ض�ȡ
		FileChannel fileChannel = new FileInputStream(file).getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

		int num = 0;
		while ((num = fileChannel.read(buffer)) > 0) {
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}

		
		
		
		Thread.sleep(10*1000);
		//�����ǵȷ���˽��귢��һ����־���ͻ���
		/*ByteBuffer revBuffer = ByteBuffer.allocate(1024);
		while (true) {
			try {
				socketChannel.read(revBuffer);
				byte[] data = buffer.array(); 
				String msg = new String(data).trim();  
		       if(msg.equals("over"))
		       {
		    	   break;
		       }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/

	}
}
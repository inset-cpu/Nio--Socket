package com.yr.nio.nio.file4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {
	public static void main(String[] args) throws Exception {
		// socketChannel �������ӷ���˵Ĺܵ�(������д��)
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.socket().connect(new InetSocketAddress("192.168.1.122", 8888));

		File file = new File("C:\\Users\\19166\\Desktop\\JDK 1.8 API.chm");
		
		String fileName= file.getName();
		
        
        
		ByteBuffer buffer = ByteBuffer.allocate(4) ;
		buffer.putInt(fileName.getBytes().length);
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		
		
		buffer = ByteBuffer.allocate(fileName.getBytes().length) ;
		buffer.put(fileName.getBytes());
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		
		
		buffer = ByteBuffer.allocate(8) ;
		buffer.putLong(file.length());
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		
		

		FileChannel fileChannel = new FileInputStream(file).getChannel();
		buffer = ByteBuffer.allocate(1024 * 1024);
		while (fileChannel.read(buffer) > 0) {
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}
		Thread.sleep(10 *1000);
	}
	
	
	//���ļ�ת����byte
		private static byte[] makeFileToByte(String fileFath) throws IOException {  
	        File file = new File(fileFath);  
	        FileInputStream fis = new FileInputStream(file);  
	        int length = (int) file.length();  
	        byte[] bytes = new byte[length];  
	        int temp = 0;  
	        int index = 0;  
	        while(true){  
	            index = fis.read(bytes,temp,length - temp);  
	            if(index <= 0 )  
	                break;  
	            temp += index;  
	        }  
	        fis.close();  
	        return bytes;  
	    }  
}
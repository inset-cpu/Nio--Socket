package com.yr.nio.nio.file3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
	public static void main(String[] args) throws Exception {
		// socketChannel �������ӷ���˵Ĺܵ�(������д��)
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));

		File file = new File("I:\\notes\\mysql\\db.zip");
		
		String fileName= file.getName();
		
        
        
		ByteBuffer buffer = ByteBuffer.allocate(4 + fileName.length() + 4 + Integer.valueOf(String.valueOf(file.length()))) ;
		buffer.putInt(fileName.length());
		buffer.put(fileName.getBytes());
		buffer.putInt(Integer.valueOf(String.valueOf(file.length())));
		buffer.put(makeFileToByte("I:\\notes\\mysql\\db.zip"));
		
		
		
		
		buffer.flip();    // �ѻ������Ķ�λָ��ʼ0��λ�� ������б��  
		socketChannel.write(buffer);  
		buffer.clear(); 
		
		
		
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
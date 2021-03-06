package com.yr.nio.zuoye.file7;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class FileClient2 {
	public static void main(String[] args) throws Exception {
		try {
			SocketChannel socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 6666);
			
			socketChannel.socket().setReuseAddress(true);
			socketChannel.connect(socketAddress);
			
			  
			 //׼�������￪ʼ���ļ�
			 String filepath = "I:\\rmi\\rmi.zip";
			 String filename = "rmi.zip";
				
			 byte[] b = makeFileToByte(filepath);
			 ByteBuffer buffer = ByteBuffer.allocate(4+filename.length()+8+b.length); 
				
			 buffer.putInt(filename.length());
			 buffer.put(filename.getBytes());
			 buffer.putLong(b.length);
			 buffer.put(b);
				
			 buffer.flip();    // �ѻ������Ķ�λָ��ʼ0��λ�� ������б��  
			 socketChannel.write(buffer);  
			 buffer.clear(); 
			 
			 
			 Thread.sleep(10*1000);
				
			    
			 System.out.println(6);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
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

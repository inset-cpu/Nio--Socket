package com.yr.nio.zuoye.file7;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class FileClient {
	public static void main(String[] args) throws Exception {
		try {
			SocketChannel socketChannel = SocketChannel.open(); 
			socketChannel.configureBlocking(false); 
			socketChannel.connect(new InetSocketAddress("localhost",6666));  
			
			Selector selector = Selector.open();  
			socketChannel.register(selector, SelectionKey.OP_CONNECT);  
			
			while (true) {  
					
				selector.select();
				  
				 //�õ����еĲ���
				 Iterator ite = selector.selectedKeys().iterator();
				 //ѭ�����в���
				 while (ite.hasNext()) {
					 //��ȡkey(����ÿ���û�)
					 SelectionKey key = (SelectionKey) ite.next();
					 
					 
					 if (key.isConnectable()) {
						 SocketChannel socketchannel = (SocketChannel) key.channel();  
		                 // ���óɷ�����  
						 socketchannel.configureBlocking(false);  
						  
						  
						 if(socketchannel.isConnectionPending()){  
							 socketchannel.finishConnect();  
		                   }  
						 socketchannel.register(selector, SelectionKey.OP_READ);
						    
					 } else if (key.isReadable()) {  
						 SocketChannel channel = (SocketChannel) key.channel();  
						  
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
						 channel.write(buffer);  
						 buffer.clear(); 
						 
						 channel.register(selector, SelectionKey.OP_WRITE); 
							
						    
						 System.out.println(6);
		              }else if(key.isWritable()){
		              }
					 
					 
					 ite.remove();
				  }
		        }  
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

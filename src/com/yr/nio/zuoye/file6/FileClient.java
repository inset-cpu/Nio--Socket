package com.yr.nio.zuoye.file6;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class FileClient {
	public static void main(String[] args) throws Exception {
		SocketChannel socketChannel = SocketChannel.open(); 
		socketChannel.connect(new InetSocketAddress("localhost",5555));
		//File.separator ����˫б��\\
		String path = "C:\\Users\\19166\\Desktop\\A.jsp";
		String fileName = "A.war";
		File file=new File(path);  
        FileInputStream fis = new FileInputStream(file);  
          
        FileChannel fic = fis.getChannel();  
          
        ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 1024 * 20);  
        ByteBuffer headbb = ByteBuffer.allocateDirect(100);  
        
        
        int read=0; 
        //�ļ�����
        long fileSize = file.length();  
        long sendSize=0;  
        System.out.println("�ļ���С��"+fileSize);  

        String head= fileName+"|"+fileSize+"|;";  
        //��ͷ��Ϣд�뻺����  
        //���ַ�������ByteBuffer�У�������socketͨ�����봫��
        headbb.put(head.getBytes(Charset.forName("UTF-8")));  
        int c=headbb.capacity()-headbb.position();  
        //����ͷ��Ϣ  
        for (int i = 0; i < c; i++) {  
            headbb.put(";".getBytes(Charset.forName("UTF-8")));  
        }  
        headbb.flip();  
        //��ͷ��Ϣд�뵽ͨ��  
        socketChannel.write(headbb);  
        do{  
            //���ļ�д�뵽������  
            read = fic.read(bb);  
            sendSize+=read;  
            bb.flip();  
            //���ļ�д�뵽ͨ��  
            socketChannel.write(bb);  
            bb.clear();  
            System.out.println("�Ѵ���/�ܴ�С��"+sendSize+"/"+fileSize);  
        } while(read != -1 && sendSize<fileSize);  
        fic.close();  
        fis.close();  
        socketChannel.close();
	}
	
}

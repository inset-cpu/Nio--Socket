package com.yr.nio.zuoye.file7;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class FileServer2 {
	public static void main(String[] args) throws Exception {
		//�򿪷������׽���ͨ��  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        //����һ��ѡ����  
        Selector selector = Selector.open();  
        //���÷�����ģʽ  
        serverSocketChannel.configureBlocking(false); 
        //�󶨼����˿�  
        serverSocketChannel.bind(new InetSocketAddress(6666));
        //ע��ѡ���������ֵȴ�ģʽ  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        while(true){  
            System.out.println("=============================" +selector.select(30*1000));  
            //���ش�ѡ��������ѡ�����  
            Set<SelectionKey> keys = selector.selectedKeys();  
            Iterator<SelectionKey> iterKey= keys.iterator();  
              
            while(iterKey.hasNext()){  
                SelectionKey sk = iterKey.next(); 
                iterKey.remove();
                //���Դ˼���ͨ���Ƿ���׼���ý����µ��׽�������  
                if(sk.isAcceptable()){  
                	System.out.println(1);
                    SocketChannel socketChannel = serverSocketChannel.accept();  
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ); 
                    
                } else if (sk.isReadable()) {
                	FileOutputStream os = null;
                	FileChannel fc = null;
                	SocketChannel sc = (SocketChannel) sk.channel();
                	System.out.println(4);
                	try {  
                		
						// �ļ�������  
				        int nameLength = 0;  
				        // �ļ���  
				        byte[] fileName = null;   // ����ļ����ֽ����鷢�������Ǿ��޴ӵ�֪�ļ����������ļ���һ�𷢹���
				        // �ļ�����   
				        long contentLength = 0;  
				        // �ļ�����   
				        byte[] contents = null;  
				        // �������ǽ���ʱǰ4���ֽ����ļ�������  
				        int capacity = 4;  
				        ByteBuffer buf = ByteBuffer.allocate(capacity);  
				        int size = 0;  
				        byte[] bytes = null;  
				        // �õ��ļ����ĳ���  
				        size = sc.read(buf);
				        System.err.println(size);
				        if (size >= 0) {
				        	System.err.println(size);
				            buf.flip();  
				            capacity = buf.getInt();  
				            buf.clear();  
				            nameLength = capacity;  
				        }  
				          
				        
				        bytes = null;
				        // ���ļ���,�����ļ���һ���ܹ�����,������ļ�������1k
				        buf = ByteBuffer.allocate(nameLength);
				        while(true)
				        {
				        	size = sc.read(buf); 
				     
					        if (size >= nameLength) {  
					            buf.flip();  
					            bytes = new byte[nameLength];  
					            buf.get(bytes);  
					            fileName = bytes;  
					            buf.clear();  
					            break;
					        } 
				        }
				          
				        // �õ��ļ�����  
				        capacity = 8;  
				        buf = ByteBuffer.allocate(capacity);  
				        size = sc.read(buf);  
				        if (size >= 0) {  
				            buf.flip();  
				            // �ļ������ǿ�Ҫ�ɲ�Ҫ�ģ������Ҫ��У���������  
				            contentLength = buf.getLong();  
				            buf.clear();  
				        }  
                    	
				        // ���ڽ���buffer�е��ֽ�����  
				        //ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				        File file = new File("C:\\Users\\Acer\\Desktop\\a\\a.zip");  
				        os = new FileOutputStream(file);  
				        //��ȡͨ��  
				        fc = os.getChannel(); 
				        // �ļ����ܻ�ܴ�  
				        buf = ByteBuffer.allocate(1024*1024);
				        int k = 0;
				        size = sc.read(buf);
				        while(size > 0){  
				        	System.out.println("k=" + (k++) + " ��ȡ��������:" + size);
				            buf.flip();  
//				            bytes = new byte[size];
				            fc.write(buf);
				            os.flush();
				            buf.clear(); 
				            size = sc.read(buf);  
				        } if(size == -1){
				        	System.out.println("fdfsaf");
				        	os.close();
					        fc.close();
	                        sc.close();
						}
                        System.out.println(5);
                    } catch (Exception e) {  
                    	e.printStackTrace();
                    }
                	
                	
				} 
            }  
        }  
	}
}

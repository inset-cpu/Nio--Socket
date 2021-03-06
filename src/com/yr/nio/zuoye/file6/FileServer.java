package com.yr.nio.zuoye.file6;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

//�̶��ļ�ͷ�����������䣩(ͷ��Ϣ�̶�100,�������Ų��䣬�ȷ�ͷ���ڷ�����)
public class FileServer {
	public static void main(String[] args) throws Exception {
		//�򿪷������׽���ͨ��  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        //����һ��ѡ����  
        Selector selector = Selector.open();  
        //���÷�����ģʽ  
        serverSocketChannel.configureBlocking(false); 
        //�󶨼����˿�  
        serverSocketChannel.bind(new InetSocketAddress(5555));
        //ע��ѡ���������ֵȴ�ģʽ  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        while(true){  
            selector.select();  
            //���ش�ѡ��������ѡ�����  
            Set<SelectionKey> keys=selector.selectedKeys();  
            Iterator<SelectionKey> iterKey=keys.iterator();  
              
            while(iterKey.hasNext()){  
                SelectionKey sk=iterKey.next();  
                //���Դ˼���ͨ���Ƿ���׼���ý����µ��׽ӽ�����
                if(sk.isAcceptable()){  
                    SocketChannel sc = serverSocketChannel.accept();  
                    try {  
                        //����  
                    	 //��ȡ�����ļ�  
                        File file=new File("C:\\Users\\19166\\Desktop\\a\\A.jsp");
                        FileOutputStream fos=new FileOutputStream(file);  
                        //��ȡͨ��  
                        FileChannel foc = fos.getChannel();  
                        //���ý�����Ϣ�建����  
                        ByteBuffer bb=ByteBuffer.allocateDirect(1024 * 1014 * 20);  
                        //���ý�����Ϣͷ������  
                        ByteBuffer headByte=ByteBuffer.allocateDirect(100);  
                        //������Ϣͷ  
                        sc.read(headByte);  
                        byte[] b=new byte[100];  
                        headByte.flip();  
                          
                        for (int i = 0; headByte.hasRemaining(); i++) {  
                            b[i]=headByte.get();  
                        }  
                        headByte.clear();  
                        //��ȡ�ļ���Ϣ  
                        String fileInfo=new String(b,Charset.forName("UTF-8"));  
                        String[] strInfo=fileInfo.split("\\|");  
                        System.out.println("�ļ���"+strInfo[0]+"--��С��"+strInfo[1]);  
                        int read=sc.read(bb);  
                          
                        while(read!=-1){  
                            bb.flip();  
                            //д�뵽���ͨ��  
                            foc.write(bb);  
                            bb.clear();  
                            read=sc.read(bb);  
                        }  
                        foc.close();  
                        fos.close();  
                    	
                        sc.close();  
                    } catch (Exception e) {  
                    }  
                }  
            }  
        }  
	}
}

package com.yr.nio.zuoye.onechiletmanyfile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NioServer {
    private static ServerSocketChannel serverSocketChannel = null;
    private static Selector selector = null;
    private final static String path = "C:\\Users\\19166\\Desktop\\a";
    // ���������Ƿ�Ϊ��һ�δ��䣬���������ְ��ճ��
    public static Map<SelectionKey, FileChannel> fileMap = new HashMap<SelectionKey, FileChannel>();
    private static long sum = 0;//�ۼӳ���
    private static long fileLength = 0;//�ļ��ܳ���
    private static String fileName =null;
    public static void main(String[] args) throws Exception {
        selector  = Selector.open();
        // �򿪷������׽���ͨ��
        serverSocketChannel = ServerSocketChannel.open();
        // ����ͨ��������ģʽ������
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            int readyChannelsNum = selector.select(2000);
            if(readyChannelsNum == 0 ){
                System.out.println("�ȴ�����--------");
                continue;
            }
            Iterator<?> iterator = selector.selectedKeys().iterator();//������
            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                if (selectionKey.isAcceptable()) {/* �ж�accept�¼� */
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {//�ж��Ƿ�ɶ��¼�
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    if(fileMap.get(selectionKey) == null){
                        ByteBuffer buf0 = ByteBuffer.allocate(4);
                        int mark = 0;
                        int size = 0;
                        while (true) {
                            size = socketChannel.read(buf0);
                            if (size >= 4) {
                                buf0.flip();
                                mark = buf0.getInt();
                                buf0.clear();
                                break;
                            }
                        }
                        //�ļ���
                        if (mark == 1 ){

                            ByteBuffer buf1 = ByteBuffer.allocate(4);
                            int fileNamelength = 0;
                            size = 0;
                            // �õ��ļ����ĳ���
                            while (true) {
                                size = socketChannel.read(buf1);
                                if (size >= 4) {
                                    buf1.flip();
                                    fileNamelength = buf1.getInt();
                                    buf1.clear();
                                    break;
                                }
                            }

                            byte[] bytes = null;
                            ByteBuffer buf2 = ByteBuffer.allocate(fileNamelength);
                            // �õ��ļ���
                            while (true) {
                                size = socketChannel.read(buf2);
                                if (size >= fileNamelength) {
                                    buf2.flip();
                                    bytes = new byte[fileNamelength];
                                    buf2.get(bytes);
                                    buf2.clear();
                                    break;
                                }
                            }
                            fileName  = new String(bytes);
                            File file = new File(path + File.separator + fileName);
                            if(!file.exists())
                            {
                                file.mkdirs();
                            }
                        }else if (mark == 2){

                            ByteBuffer buf1 = ByteBuffer.allocate(4);
                            int fileNamelength = 0;
                            size = 0;
                            // �õ��ļ����ĳ���
                            while (true) {
                                size = socketChannel.read(buf1);
                                if (size >= 4) {
                                    buf1.flip();
                                    fileNamelength = buf1.getInt();
                                    buf1.clear();
                                    break;
                                }
                            }

                            byte[] bytes = null;
                            ByteBuffer buf2 = ByteBuffer.allocate(fileNamelength);
                            // �õ��ļ���
                            while (true) {
                                size = socketChannel.read(buf2);
                                if (size >= fileNamelength) {
                                    buf2.flip();
                                    bytes = new byte[fileNamelength];
                                    buf2.get(bytes);
                                    buf2.clear();
                                    break;
                                }
                            }
                            fileName = new String(bytes);

                            ByteBuffer buf3 = ByteBuffer.allocate(8);
                            //�ļ����ݳ���
                            while (true) {
                                size = socketChannel.read(buf3);
                                if (size >= 8) {
                                    buf3.flip();
                                    // �ļ������ǿ�Ҫ�ɲ�Ҫ�ģ������Ҫ��У���������
                                    fileLength = buf3.getLong();

                                    buf3.clear();

                                    break;
                                }
                            }
                            ByteBuffer buf24 = null;
                            if (fileLength - sum < 1024 * 1024) {
                                buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(fileLength - sum)));

                            } else {
                                buf24 = ByteBuffer.allocate(1024 * 1024);
                            }
                            socketChannel.read(buf24);
                            String paths =  path+ File.separator + fileName;
                            //�ļ��ܵ�
                            FileChannel fileContentChannel = new FileOutputStream(new File(paths)).getChannel();
                            buf24.flip();
                            long a = fileContentChannel.write(buf24);
                            buf24.clear();

                            sum = sum + a;

                            if (sum == fileLength) {
                                sum = 0;
                                fileLength = 0;
                                fileMap.put(selectionKey, null);
                                fileContentChannel.close();
                            } else {
                                fileMap.put(selectionKey, fileContentChannel);
                            }
                        }
                    }else {
                        ByteBuffer buf24 = null;
                        if (fileLength - sum < 1024 * 1024) {
                            buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(fileLength - sum)));

                        } else {
                            buf24 = ByteBuffer.allocate(1024 * 1024);

                        }
                        socketChannel.read(buf24);// ÿ�ζ�ȡ�ĳ���

                        // String path = DIRECTORY + File.separator + fileName;
                        // FileChannel fileContentChannel = new FileOutputStream(new
                        // File(path)).getChannel();
                        FileChannel fileContentChannel = fileMap.get(selectionKey);
                        buf24.flip();
                        long a = fileContentChannel.write(buf24);
                        sum = sum + a;
                        buf24.clear();

                        if (sum == fileLength) {
                            sum = 0;
                            fileLength = 0;
                            fileMap.put(selectionKey, null);
                            System.out.println(fileName+"���ճɹ�");
                            fileContentChannel.close();
                        }
                    }
                }

                iterator.remove();

            }
        }
    }
}

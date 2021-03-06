package com.yr.nio.zuoye.manychilemanyfile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class ServerThread {
    //�ļ�·��
    private final  static  String filePath="C:\\Users\\19166\\Desktop\\a";
    //�ļ��ܵ�
    private static Map<SelectionKey , FileChannel> fileChannelMap = new HashMap<SelectionKey, FileChannel>();
    //�ļ�����
    private static Map<SelectionKey , Long > filelengthMap = new HashMap<SelectionKey, Long>();
    //�ۼӳ���
    private static Map<SelectionKey , Long > accumulationMap = new HashMap<SelectionKey, Long>();
    //�����ļ���
    private static Map<SelectionKey, String> fileNameMap = new HashMap<SelectionKey, String>();

    private static  String  fileName=null;

    public static void fuck(SelectionKey key) throws  Exception{
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            if (fileChannelMap.get(key) == null) {
                int make = 0;
                int size = 0;
                //��ʶ
                ByteBuffer buffer = ByteBuffer.allocate(4);
                while (true) {
                    size = socketChannel.read(buffer);
                    if (size >= 4) {
                        buffer.flip();
                        make = buffer.getInt();
                        buffer.clear();
                        break;
                    }
                }
                if (make == 1) {
                    ByteBuffer buffer1 = ByteBuffer.allocate(4);
                    int fileNamelength = 0;
                    // �õ��ļ����ĳ���
                    while (true) {
                        size = socketChannel.read(buffer1);
                        if (size >= 4) {
                            buffer1.flip();
                            fileNamelength = buffer1.getInt();
                            buffer1.clear();
                            break;
                        }
                    }
                    //�ļ���
                    byte[] bytes = null;
                    ByteBuffer buffer2 = ByteBuffer.allocate(fileNamelength);
                    while (true) {
                        size = socketChannel.read(buffer2);
                        if (size >= fileNamelength) {
                            buffer2.flip();
                            bytes = new byte[fileNamelength];
                            buffer2.get(bytes);
                            buffer2.clear();
                            break;
                        }
                    }
                    fileName = new String(bytes);
                    fileNameMap.put(key, fileName);


                    File file = new File(filePath + File.separator + fileName);
                    if (!file.exists()) {
                            file.mkdirs();
                    }
                } else if (make == 2) {

                    ByteBuffer buf1 = ByteBuffer.allocate(4);
                    int fileNamelength = 0;
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
                    fileNameMap.put(key, fileName);

                    long fileLengh = 0L;
                    ByteBuffer buf3 = ByteBuffer.allocate(8);
                    while (true) {
                        size = socketChannel.read(buf3);
                        if (size >= 8) {
                            buf3.flip();
                            // �ļ������ǿ�Ҫ�ɲ�Ҫ�ģ������Ҫ��У���������
                            fileLengh = buf3.getLong();
                            filelengthMap.put(key, fileLengh);
                            buf3.clear();
                            break;
                        }
                    }
                    accumulationMap.put(key, 0L);

                    ByteBuffer buf24 = null;
                    if (filelengthMap.get(key) - accumulationMap.get(key) < 1024 * 1024) {
                        buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(filelengthMap.get(key) - accumulationMap.get(key))));
                    } else {
                        buf24 = ByteBuffer.allocate(1024 * 1024);
                    }
                    socketChannel.read(buf24);
                    String path = filePath + File.separator + fileName;
                    FileChannel fileContentChannel = new FileOutputStream(new File(path)).getChannel();
                    buf24.flip();
                    long a = fileContentChannel.write(buf24);
                    buf24.clear();

                    // a = (accumulationMap.get(key) == null ? 0 : accumulationMap.get(key)) + a;
                    accumulationMap.put(key, accumulationMap.get(key) + a);
                    if (accumulationMap.get(key).longValue() == filelengthMap.get(key).longValue()) {
                        accumulationMap.put(key, 0L);
                        filelengthMap.put(key, 0L);
                        fileChannelMap.put(key, null);
                        fileContentChannel.close();
                    }else {
                        fileChannelMap.put(key, fileContentChannel);
                        //filelengthMap.put(key, fileLengh);
                    }
                }
            } else {
                ByteBuffer buf24 = null;
                if (filelengthMap.get(key) - accumulationMap.get(key) < 1024 * 1024) {
                    buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(filelengthMap.get(key) - accumulationMap.get(key))));
                } else {
                    buf24 = ByteBuffer.allocate(1024 * 1024);
                }
                socketChannel.read(buf24);
                FileChannel fileContentChannel = fileChannelMap.get(key);
                buf24.flip();
                long a = fileContentChannel.write(buf24);
                accumulationMap.put(key, accumulationMap.get(key) + a);
                buf24.clear();
                if (accumulationMap.get(key).longValue() == filelengthMap.get(key).longValue()) {
                    accumulationMap.put(key, 0L);
                    filelengthMap.put(key, 0L);
                    fileChannelMap.put(key, null);
                    fileContentChannel.close();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
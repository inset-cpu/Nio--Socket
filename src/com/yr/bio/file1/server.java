package com.yr.bio.file1;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private static DataInputStream input;
    private static FileOutputStream out;
    private static  ServerSocket ss;
    public static void stup() throws IOException {
        try{
            ss = new ServerSocket(8080);

            while (true) {
                // 调用accept()方法开始监听，等待客户端的连接
                Socket sk =ss.accept();
                //这里使用DataInputStream，可以调用它的readUTF方法来读取要传输的文件名，客户端使用writeUTF方法将文件名先传输过来
                input = new DataInputStream(sk.getInputStream());
                //首先读取文件名   得到文件名称
                String fileName = input.readUTF();
                //获取的文件存放位置
                File file = new File("C:\\Users\\19166\\Desktop\\"+fileName);
                //如果没有这个文件就创建
                if(file.exists()) {
                    file.createNewFile();
                }

                out = new FileOutputStream(file); //利用FileOutputStream来操作文件输出流

                byte[] by= new byte[1024];
                int length=0;
                while ((length = input.read(by)) != -1 ) {
                    out.write(by,0,length);
                    out.flush();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out != null) {
                out.close();
                ss.close();
            }
            if(input != null) {
                input.close();

            }
        }

    }
    public static void main(String[] args) {
        try {
            stup();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

package com.yr.bio.file1;

import java.io.*;
import java.net.Socket;

public class client {

  private static  DataInputStream input;
    private  static DataOutputStream out;
    private static Socket sk;
    public static  void fli() throws Exception{

        try {

            sk = new Socket("192.168.1.143",8080);
            //向服务端传送文件  文件路径
            String filephth ="C:\\Users\\19166\\Desktop\\head\\1.jpg";

            File file = new File(filephth);
            //输入流是用来读取本地文件
            input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //输出文件
            out = new DataOutputStream(sk.getOutputStream());
            // out.writeUTF(fileName);   客户端发送使用writeUTF方法，服务器端应该使用readUTF方法
            //之后再发送文件的内容
            //首先发送文件名,文件长度
            out.writeUTF(file.getName());
            //by，一次读取或写入多个字节的数据
            byte[] by = new byte[1024];

            int length = 0;
            //根据长度循环发送
            while ((length = input.read(by, 0, by.length)) > 0) {
                out.write(by, 0, length);
                //刷新
                out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(out != null ) {
                //关闭输出流
                out.close();
                //关闭socket
                sk.close();
            }

            if(input != null ) {
                //关闭输入流
                input.close();

            }
        }
    }
    public static void main(String[] args) {
        try {
            fli();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

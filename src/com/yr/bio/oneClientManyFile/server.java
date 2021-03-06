package com.yr.bio.oneClientManyFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private static String path="C:\\Users\\19166\\Desktop\\a\\";
    public static void main(String[] args) throws IOException {
        server.serverfile();
    }
    public static void serverfile() throws IOException {
                                    // 定义一个端口,和客户端保持一致
        ServerSocket serverSocket = new ServerSocket(8888);// 构建一个Serversocket
        //我们这里只接收一个客户端
        Socket socket = serverSocket.accept();// 监听客户端
        DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        while (true) {
            /**
             * mark标识，1文件夹，2文件
             */
            int make = 0;
            while (true) {
                if (input.available() >= 4) {//判断是否有值传输过来
                    make = input.readInt();// 得到客户端写过来的文件长度, 4个字节
                    break;//终止
                }
            }
            if(make == 1){//当make等于一就是文件夹
                int lengths = 0;//定义一个文件长度
                while (true){//保证可读长度够用
                    if(input.available()>=4){//判断是否有值传输过来
                        lengths = input.readInt();//得到客户端写过来的文件名称, 4个字节
                        break;//终止
                    }
                }
                String filespath  = null;
                //取文件路径
                byte pathname [] = new byte[lengths];// 转换成byte数组
                int files = 0;
                while (true){// 直到将文件名的长度全部读取完成
                    files += input.read(pathname);//累加
                    if(lengths == files){//当接取的文件名和发送的文件名长度一样时，就结束循环
                        break;//终止
                    }
                }
                filespath = new String(pathname);//得到文件名
                File file = new File(path+filespath);//得到文件夹路径
                if(!file.exists()){//判断是否有这个文件夹路径
                    file.mkdir();//创建文件夹
                }
            }else if (make == 2 ){//make = 2 是文件
                int filelength =0;//定义一个文件名的长度
                while(true){
                    if(input.available() >= 4){//判断是否有值传输过来
                        filelength = input.readInt();// 得到客户端写过来的文件名字长度, 4个字节
                        break;//终止
                    }
                }
                byte filepath[] = new byte[filelength];// 转换成byte数组
                String filenames =  null ;
                int filelengths = 0;
                while (true){
                    filelengths += input.read(filepath);//累加文件名长度
                    if (filelength == filelengths){// 直到将文件名的长度全部读取完成
                        break;
                    }
                }
                filenames = new String(filepath);//得到文件名
                File fil = new File(path + filenames);//得到文件路径
                if(!fil.exists()){//判断文件路径是否存在
                    fil.createNewFile();//创建文件
                }
                //文件内容长度
                long fileAlllength = 0L;
                //累加长度
                long fileat = 0L;
                while (true){
                    if(input.available() >= 8 ){// 得到客户端写过来的文件内容长度, 8个字节
                        fileAlllength +=  input.readLong();// 直到将文件内容长度全部读取完成
                        break;
                    }
                }
                byte bt [] = new byte[1024 * 1024];// 每次读取20M
                DataOutputStream out = new DataOutputStream(new FileOutputStream(fil));
                int len = 0;
                while (true){
                    if(fileAlllength == fileat){// 如果客户端写过来的文件长度等于我们要读取的文件长度就结束循环
                        break;
                    }
                    if (fileAlllength - fileat < input.available()){
                        len = input.read(bt,0,Integer.valueOf(String.valueOf((fileAlllength - fileat))));
                    }else{
                        if ((fileAlllength-fileat) >(1024 * 1024)){
                            len=input.read(bt);//每次读
                        }else{
                            len = input.read(bt,0,Integer.valueOf(String.valueOf((fileAlllength - fileat))));
                        }
                    }
                    fileat += len;
                    out.write(bt,0,len);
                    out.flush();
                }
                System.out.println( filenames+ "接收完成了...");
                out.close();
            }
        }
    }
}
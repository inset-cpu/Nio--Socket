package com.yr.bio.oneClientManyFile;

import java.io.*;
import java.net.Socket;

public class chlient {
    private static Socket socket;
    private static String path ="D:\\学习文档\\2javaBase\\";
    private static DataOutputStream out;
    public static void main(String[] args) throws IOException, InterruptedException {

        socket = new Socket("192.168.1.143", 8888);
        // 由Socket对象得到输入流,并构造相应的DataOutputStream对象
        out = new  DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            chlient.filepath(path);
            Thread.sleep(10000);
            //如果服务端接收成功，返回一个消息．告诉客户端
    }
    public static void  filepath (String filpath) throws IOException {
        File file = new File(filpath);// new一个 file 对象
        if(file.isDirectory()){// 判断是否为文件夹
            if(!filpath.equals(path)) {//不发送最基础目录
                out.writeInt(1);//1表示是文件夹
                String pa = file.getPath();// 获取文件全路径
                String ps = pa.replace(path, "");// 替换基础路径
                out.writeInt(new String(ps.getBytes(), "ISO-8859-1").length());// 发送路径长度
                out.write(ps.getBytes());// 发送路径
                out.flush();//刷新
            }
            //递归
            String filelist [] =  file.list();
            for(String pathe:filelist) {
                filepath(filpath + File.separator + pathe);
            }
        }else{
            out.writeInt(2);// 表示文件
            String pa = file.getPath();// 获取文件内容全路径
            String ps = pa.replace(path,"");// 替换基础路径
            out.writeInt(new String(ps.getBytes(),"ISO-8859-1").length());// 发送文件内容路径长度
            out.write(ps.getBytes());// 发送文件路径
            long fil = file.length();//获取文件内容长度
            out.writeLong(fil);// 发送文件内容长度
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] by = new byte[1024 * 1024];// new一个byte数组
            int leng = 0;//可读取的长度
            while ((leng = input.read(by)) > 0){// 只要文件内容的长度大于0,就一直读取
                out.write(by,0,leng);// 将读取的文件内容写过去
                out.flush();//刷新
            }
            input.close();//关闭流
        }
    }
}
package com.yr.bio.manyClientManyFile;

import java.io.*;
import java.net.Socket;

public class cilent2 {
    private  static  String path="E:\\WeGame\\";
    private static DataInputStream input = null;
    private static DataOutputStream out = null;
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("192.168.1.143",8080);
        out= new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        Sos(path);
        Thread.sleep(10000);
    }
    public static void Sos (String filepath) throws IOException {
        File file = new File(filepath);
        if(file.isDirectory()){// 判断是否为文件夹
            if(!filepath.equals(path)) {//不发送最基础目录
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
                Sos(filepath + File.separator + pathe);
            }
        }else {
            out.writeInt(2);// 表示文件
            String pa = file.getPath();// 获取文件内容全路径
            String ps = pa.replace(path, "");// 替换基础路径
            out.writeInt(new String(ps.getBytes(), "ISO-8859-1").length());// 发送文件内容路径长度
            out.write(ps.getBytes());// 发送文件路径
            long fil = file.length();//获取文件内容长度
            out.writeLong(fil);// 发送文件内容长度
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            byte[] by = new byte[1024 * 1024];// new一个byte数组
            int leng = 0;//可读取的长度
            while ((leng = input.read(by)) > 0) {// 只要文件内容的长度大于0,就一直读取
                out.write(by, 0, leng);// 将读取的文件内容写过去
                out.flush();//刷新
            }
            out.flush();//刷新
            input.close();//关闭流
        }
    }
}

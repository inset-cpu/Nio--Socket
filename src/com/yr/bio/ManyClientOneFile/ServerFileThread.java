package com.yr.bio.ManyClientOneFile;

import java.io.*;
import java.net.Socket;

public class ServerFileThread extends Thread{
    private Socket socket;
    private static String path= "C:\\Users\\19166\\Desktop\\a\\";
    public ServerFileThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = null;
            DataOutputStream out = null;
            input = new DataInputStream (new BufferedInputStream((socket.getInputStream())));
            int make = 0;
            while (true) {
                if ((input.available()) >= 4) {
                    make = input.readInt();
                    break;
                }
            }
            if (make == 1) {
                int filepath = 0;
                while (true) {
                    if (input.available() >= 4) {
                        filepath = input.readInt();
                        break;
                    }
                }
                byte fileName[] = new byte[filepath];
                String pathName = null;
                int pathLength = 0;
                while (true) {
                    pathLength += input.read(fileName);
                    if (filepath == pathLength) {
                        break;
                    }
                }
                pathName = new String(fileName);
                File file = new File(path + pathName);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } else if (make == 2) {
                int fileName = 0;
                while (true) {
                    if ((input.available()) >= 4) {
                        fileName = input.readInt();
                        break;
                    }
                }
                byte name[] = new byte[fileName];
                String fileNames = null;
                int fileleng = 0;
                while (true) {
                    fileleng += input.read(name);
                    if (fileName == fileleng) {
                        break;
                    }
                }
                fileNames = new String(name);
                File file = new File(path + fileNames);
                if (!file.exists()) {
                    File file1 = new File(file.getParent());
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    file.createNewFile();
                }
                //文件内容长度
                long fielLength = 0L;
                //累加长度
                long fielLengths = 0L;
                while (true) {
                    if (input.available() >= 8) {// 得到客户端写过来的文件内容长度, 8个字节
                        fielLength += input.readLong();// 直到将文件内容长度全部读取完成
                        break;
                    }
                }
                byte fileContent[] = new byte[1024 * 1024];
                out = new DataOutputStream(new FileOutputStream(file));
                int lens = 0;
                while (true) {
                    if (fielLength == fielLengths) {// 如果客户端写过来的文件长度等于我们要读取的文件长度就结束循环
                        break;
                    }
                    if (fielLength - fielLengths < input.available()) {
                        lens = input.read(fileContent, 0, Integer.valueOf(String.valueOf((fielLength - fielLengths))));
                    } else {
                        if ((fielLength - fielLengths) > (1024 * 1024)) {
                            lens = input.read(fileContent);//每次读
                        } else {
                            lens = input.read(fileContent, 0, Integer.valueOf(String.valueOf((fielLength - fielLengths))));
                        }
                    }
                    fielLengths += lens;
                    out.write(fileContent, 0, lens);
                    out.flush();
                }
                System.out.println(fileNames + "接收完成了...");
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ServerFile.make--;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

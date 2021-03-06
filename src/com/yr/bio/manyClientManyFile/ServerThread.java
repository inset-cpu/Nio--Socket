package com.yr.bio.manyClientManyFile;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private static  String path = "C:\\Users\\19166\\Desktop\\a\\";
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket=socket;
    }
    @Override

    public void run() {
        try {
        DataInputStream input=null;
        DataOutputStream out = null;
        input = new  DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while (true){
                int make = 0;
                while (true){
                    if(input.available()>=4) {
                        make = input.readInt();
                        break;
                    }
                }
                if(make == 1){
                    int filepath = 0;
                    while (true){
                        if(input.available() >= 4){
                            filepath = input.readInt();
                            break;
                        }
                    }
                    byte fileName []= new byte[filepath];
                    String pathName = null;
                    int pathLength =0 ;
                    while (true){
                        pathLength += input.read(fileName);
                        if(filepath == pathLength){
                            break;
                        }
                    }
                    pathName = new String(fileName);
                    File file = new File(path+pathName);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                }else if (make ==2){
                    int fileName = 0;
                    while (true){
                        if((input.available()) >= 4){
                            fileName = input.readInt();
                            break;
                        }
                    }
                    byte name [] = new byte[fileName];
                    String fileNames = null;
                    int fileleng =0;
                    while (true){
                        fileleng += input.read(name);
                        if(fileName == fileleng){
                            break;
                        }
                    }
                    fileNames = new String(name);
                    File file = new File(path+fileNames);
                    if(!file.exists()){
                        File file1 = new File(file.getParent());
                        if (!file1.exists()){
                            file1.mkdirs();
                        }
                        file.createNewFile();
                    }
                    //??????????????????
                    long fielLength = 0L;
                    //????????????
                    long fielLengths = 0L;
                    while (true){
                        if(input.available() >= 8 ){// ?????????????????????????????????????????????, 8?????????
                            fielLength +=  input.readLong();// ?????????????????????????????????????????????
                            break;
                        }
                    }
                    byte fileContent [] = new byte[1024 * 1024];
                    out = new DataOutputStream(new FileOutputStream(file));
                    int lens = 0;
                    while (true){
                        if(fielLength == fielLengths){// ??????????????????????????????????????????????????????????????????????????????????????????
                            break;
                        }
                        if (fielLength - fielLengths < input.available()){
                            lens = input.read(fileContent,0,Integer.valueOf(String.valueOf((fielLength - fielLengths))));
                        }else{
                            if ((fielLength-fielLengths) >(1024 * 1024)){
                                lens=input.read(fileContent);//?????????
                            }else{
                                lens = input.read(fileContent,0,Integer.valueOf(String.valueOf((fielLength - fielLengths))));
                            }
                        }
                        fielLengths += lens;
                        out.write(fileContent,0,lens);
                        out.flush();
                    }
                    System.out.println( fileNames+ "???????????????...");
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}

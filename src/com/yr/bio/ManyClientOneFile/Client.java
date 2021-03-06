package com.yr.bio.ManyClientOneFile;

import java.io.File;

public class Client {
    private static String path = "E:\\英雄联盟安装目录\\";
    public static int make = 0;

    public static void main(String[] args) throws Exception {
        send(path);
        Thread.sleep( 10 * 100000);
    }
    public static void send (String paths ) throws InterruptedException {
        while (true){
            if(make > 5){
                Thread.sleep(5);
            }else {
                break;
            }
        }
        File file = new  File(paths);
        if(file.isDirectory()){
            if(!paths.equals(path)){
                System.out.println(file.getPath());
                CilentThread cilentThread = new CilentThread(file.getPath());
                    make++;
                cilentThread.start();
                cilentThread.join();//等到线程执行完
            }
            String pathAll [] =  file.list();
            for (String filepath : pathAll) {
                send(paths + File.separator + filepath);
            }
        }else{
            System.out.println(file.getPath());
            CilentThread thread = new CilentThread(file.getPath());
            make++;
            thread.start();
        }
    }
}
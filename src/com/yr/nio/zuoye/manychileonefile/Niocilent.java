package com.yr.nio.zuoye.manychileonefile;

import java.io.File;

public class Niocilent {
    private static String  paths ="D:\\eclipse-4.11";
    public static int a = 0;
    public static void main(String[] args) throws  Exception{

        Niocilent.fileclient(paths);
        Thread.sleep( 10 * 100000);

    }
    public static void fileclient(String filepath) throws  Exception{
        while (true){
            if(a > 5){
                Thread.sleep(10);
            }else {
                break;
            }
        }
        File file = new File(filepath);
        if(file.isDirectory()){
            if(!filepath.equals(paths)){
                CilentThread2 cilentThread2 = new CilentThread2(filepath,paths);
                a++;
                cilentThread2.start();
                cilentThread2.join();
            }
            String files[] = file.list();
            for (String sfile : files) {
                fileclient(filepath+File.separator+sfile);
                System.out.println(sfile);
            }
        }else {
            a++;
             new CilentThread1(filepath,paths).start();
        }
    }
}

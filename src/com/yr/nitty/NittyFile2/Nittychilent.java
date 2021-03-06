package com.yr.nitty.NittyFile2;


import java.io.File;

public class Nittychilent {
    public static void main(String[] args) {
        Nittychilent lent = new Nittychilent();
        lent.Dire(new File("D:\\学习文档\\zj\\cours"));
    }

    public void Dire(File file) {
        try {
            File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
            for (File files:fileArray) {//循环文件下的所有文件
                if (files.isDirectory()) {//是否是文件夹
                    ChilentThread c = new ChilentThread(files.getPath(),0);
                    System.out.println(files.getPath());
                    c.start();
                    c.join();
                    Dire(files);
                } else {//否则是文件
                    ChilentThread c = new ChilentThread(files.getPath(),1);
                    System.out.println(files.getPath());
                    c.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.yr.bio.ManyClientOneFile;

import java.io.*;
import java.net.Socket;

public class CilentThread extends  Thread{
    private  String filepath;
    private static String paths ="E:\\英雄联盟安装目录\\";

    public CilentThread(String filepath){
        this.filepath=filepath;
    }
    @Override
    public void run() {
        try {
            Socket socket  = new Socket("192.168.1.143", 8888);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            File file = new File(filepath);
            if (file.isDirectory()){
                if (!filepath.equals(paths)){
                    out.writeInt(1);
                    String fileAll = file.getPath();
                    String fuck = fileAll.replace(paths,"");
                    out.writeInt(new String(fuck.getBytes(),"ISO-8859-1").length());
                    out.write(fuck.getBytes());
                    out.flush();
                }
//                //递归
//                String filelist [] =  file.list();
//                for(String pathe:filelist) {
//                    filepath(paths + File.separator + pathe);
//                }
            }else {
                out.writeInt(2);
                String filegetAll =  file.getPath();
                String filegetPath= filegetAll.replace(paths,"");
                out.writeInt(new String(filegetPath.getBytes(),"ISO-8859-1").length());
                out.write(filegetPath.getBytes());
                long files = file.length();
                out.writeLong(files);
                DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                byte bt[] = new byte[1024 * 1024];
                int lengs = 0;
                while ((lengs = input.read(bt)) > 0){
                    out.write(bt,0,lengs);
                    out.flush();
                }
                out.flush();
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Client.make--;
        }
    }
}
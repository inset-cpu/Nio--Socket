package com.yr.bio.ManyClientOneFile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile {
    public static volatile int make = 1;
    public static void main(String[] args) throws IOException {
        ServerFile serverFile = new ServerFile();
        int prot = 8888;
        serverFile.send(prot);
    }
    public static void send(int  port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = null;
        while (true){
            socket = serverSocket.accept();
            while (true){
                if(make > 5){
                    //Thread.sleep(100);
                }else{
                    break;
                }
            }
            ServerFileThread serverFileThread = new ServerFileThread(socket);
            make++;
            serverFileThread.start();
        }
    }
}

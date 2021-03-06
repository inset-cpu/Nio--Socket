package com.yr.bio.manyClientManyFile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static volatile int make = 1;
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        int prot = 8080;

        server.send(prot);
    }
    public static void send(int  port) throws IOException, InterruptedException {

        
        ServerSocket serverSocket = new ServerSocket(port);// 构建一个Serversocket

        while(true)
        {

            while(true)
            {
                if(make > 5)
                {
                    Thread.sleep(10);
                }
                else
                {
                    break;
                }
            }
            Socket socket = serverSocket.accept();// 监听客户端
            new ServerThread(socket).start();//开启线程
        }

    }
}

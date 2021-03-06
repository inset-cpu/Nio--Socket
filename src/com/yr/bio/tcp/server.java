package com.yr.bio.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(8080);
		Socket so  = socket.accept();
		InputStreamReader input = new InputStreamReader(so.getInputStream());
		BufferedReader but = new BufferedReader(input);
		String info = null ;
		while ((info=but.readLine()) != null) {
			 System.out.println("客户端发送过来的消息："+info);
		   }
		   System.out.println("服务端接收消息成功");
		   input.close();
		   socket.close();
	}
}
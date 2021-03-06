package com.yr.bio.file2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) throws Exception {
		ServerSocket ser = new ServerSocket(8080);
		System.out.println("服务启动成功 ");
		while (true) {
			Socket s = ser.accept();
			BufferedReader but = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("你输入的是 : " + but.readLine());
		}
	}
}

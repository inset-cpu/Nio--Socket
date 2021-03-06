package com.yr.bio.file2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class client2 {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("192.168.1.143",8080);
		PrintWriter pr =new PrintWriter(socket.getOutputStream());
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入：");
		pr.write(bf.readLine());
		pr.close();
		bf.close();
	}
}

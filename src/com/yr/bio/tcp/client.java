package com.yr.bio.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("192.168.1.143",8080);
		OutputStream ot = socket.getOutputStream();
		String  s ="钟科到家";
		ot.write(s.getBytes());
		System.out.println("消息已经送到");
		ot.close();
		socket.close();
			
	}
}
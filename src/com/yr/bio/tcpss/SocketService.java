package com.yr.bio.tcpss;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(8080);
		// 调用accept()方法开始监听，等待客户端的连接
		Socket socket=server.accept();

		DataInputStream dataInput = new DataInputStream(socket.getInputStream());

		//可读长度
		byte[] by = new byte[dataInput.available()];
		dataInput.read(by);
		System.out.println("服务端接收消息成功:");
		String a = new String(by);
		System.out.println(a);

		DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
		dataOutput.write("钟科吃似".getBytes());
		System.out.println("服务端发送消息成功:");
		dataOutput.flush();
		dataOutput.close();
		dataInput.close();
		server.close();
	}
}

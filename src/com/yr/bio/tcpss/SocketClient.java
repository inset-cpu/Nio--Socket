package com.yr.bio.tcpss;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketClient {
	public static void main(String[] args) throws Exception {
		Socket socket=new Socket("192.168.1.143", 8080);
		DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
		dataOutput.write("钟科".getBytes());
		dataOutput.flush();
		System.out.println("客户端发送成功");

		Thread.sleep(1 * 1000);
		DataInputStream dataInput = new DataInputStream(socket.getInputStream());
		byte[] by = new byte[dataInput.available()];//可读长度
		dataInput.read(by);
		System.out.println("客户端接收信息成功:");
		System.out.println(new String(by));

		dataInput.close();
		dataOutput.close();
		socket.close();
	}
}
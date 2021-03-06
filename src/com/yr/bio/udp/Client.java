package com.yr.bio.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Client {
	public static void main(String[] args) {
		 DatagramSocket dt = null ;
		try {
			dt = new DatagramSocket();
			String hello = "钟科吃死";
			DatagramPacket dp = new DatagramPacket(hello.getBytes(), hello.getBytes().length,
					new InetSocketAddress("127.0.0.1",8080));
			for (int i = 0; i < 10; i++) {
				dt.send(dp);
				Thread.sleep(1000);  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(dt != null) dt.close();
		}
	}
}

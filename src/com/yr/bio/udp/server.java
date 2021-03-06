package com.yr.bio.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class server {
	public static void main(String[] args) {
		DatagramSocket dt = null;
		try {
			dt =new DatagramSocket(8080);
			byte[] dy =  new byte[1024];
			DatagramPacket dp =new DatagramPacket(dy, dy.length);
			while (true) {
				dt.receive(dp);
				String s = new String(dp.getData(),0,dp.getLength());
				 System.out.println("length:" + dp.getLength() + "****" + s);  
			}
		} catch (SocketException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        } finally {  
			if(dt != null)
				dt.close();
		}
	}
}

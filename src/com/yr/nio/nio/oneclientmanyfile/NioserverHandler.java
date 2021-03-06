package com.yr.nio.nio.oneclientmanyfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class NioserverHandler {

	private final static Logger logger = Logger.getLogger(NioserverHandler.class.getName());
	private final static String DIRECTORY = "C:\\Users\\Acer\\Desktop\\a";

	/**
	 * 这里边我们处理接收和发送
	 * 
	 * @param serverSocketChannel
	 * 
	 */
	public void excute(SelectionKey s) {
		try {
			receiveData(s);// 接数据

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取通道中的数据到Object里去
	 * 
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */

	// 用来区分是否为第一次传输，还用来区分半包粘包
	public static Map<SelectionKey, FileChannel> fileMap = new HashMap<SelectionKey, FileChannel>();

	private static long sum = 0;
	private static long fileLength = 0;
	public static String fileName = null;

	public void receiveData(SelectionKey s) throws IOException {

		SocketChannel socketChannel = (SocketChannel) s.channel();

		if (fileMap.get(s) == null) {

			// 标识
			ByteBuffer buf0 = ByteBuffer.allocate(4);
			int mark = 0;
			int size = 0;
			while (true) {
				size = socketChannel.read(buf0);
				if (size >= 4) {
					buf0.flip();
					mark = buf0.getInt();
					buf0.clear();
					break;
				}
			}
			if (mark == 1) {
				
				ByteBuffer buf1 = ByteBuffer.allocate(4);
				int fileNamelength = 0;
				size = 0;
				// 拿到文件名的长度
				while (true) {
					size = socketChannel.read(buf1);
					if (size >= 4) {
						buf1.flip();
						fileNamelength = buf1.getInt();
						buf1.clear();
						break;
					}
				}

				byte[] bytes = null;
				ByteBuffer buf2 = ByteBuffer.allocate(fileNamelength);
				while (true) {
					size = socketChannel.read(buf2);
					if (size >= fileNamelength) {
						buf2.flip();
						bytes = new byte[fileNamelength];
						buf2.get(bytes);
						buf2.clear();
						break;
					}

				}
				fileName = new String(bytes);
				
				File file = new File(DIRECTORY + File.separator + fileName);
				if(!file.exists())
				{
					file.mkdirs();
				}

			} else if (mark == 2) {

				ByteBuffer buf1 = ByteBuffer.allocate(4);
				int fileNamelength = 0;
				size = 0;
				// 拿到文件名的长度
				while (true) {
					size = socketChannel.read(buf1);
					if (size >= 4) {
						buf1.flip();
						fileNamelength = buf1.getInt();
						buf1.clear();
						break;
					}
				}

				byte[] bytes = null;
				ByteBuffer buf2 = ByteBuffer.allocate(fileNamelength);
				while (true) {
					size = socketChannel.read(buf2);
					if (size >= fileNamelength) {
						buf2.flip();
						bytes = new byte[fileNamelength];
						buf2.get(bytes);
						buf2.clear();
						break;
					}

				}
				fileName = new String(bytes);

				ByteBuffer buf3 = ByteBuffer.allocate(8);
				while (true) {
					size = socketChannel.read(buf3);
					if (size >= 8) {
						buf3.flip();
						// 文件长度是可要可不要的，如果你要做校验可以留下
						fileLength = buf3.getLong();

						buf3.clear();

						break;
					}

				}

				// 5,a.txt,10000,1000
				ByteBuffer buf24 = null;
				if (fileLength - sum < 1024 * 1024) {
					buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(fileLength - sum)));

				} else {
					buf24 = ByteBuffer.allocate(1024 * 1024);

				}
				socketChannel.read(buf24);
				String path = DIRECTORY + File.separator + fileName;
				FileChannel fileContentChannel = new FileOutputStream(new File(path)).getChannel();
				buf24.flip();
				long a = fileContentChannel.write(buf24);
				buf24.clear();

				sum = sum + a;

				if (sum == fileLength) {
					sum = 0;
					fileLength = 0;
					fileMap.put(s, null);
					fileContentChannel.close();
				} else {
					fileMap.put(s, fileContentChannel);
				}
			}
		} else {
			ByteBuffer buf24 = null;
			if (fileLength - sum < 1024 * 1024) {
				buf24 = ByteBuffer.allocate(Integer.valueOf(String.valueOf(fileLength - sum)));

			} else {
				buf24 = ByteBuffer.allocate(1024 * 1024);

			}

			socketChannel.read(buf24);// 每次读取的长度

			// String path = DIRECTORY + File.separator + fileName;
			// FileChannel fileContentChannel = new FileOutputStream(new
			// File(path)).getChannel();
			FileChannel fileContentChannel = fileMap.get(s);
			buf24.flip();
			long a = fileContentChannel.write(buf24);
			sum = sum + a;
			buf24.clear();

			if (sum == fileLength) {
				sum = 0;
				fileLength = 0;
				fileMap.put(s, null);

				fileContentChannel.close();
			}

		}

	}

}
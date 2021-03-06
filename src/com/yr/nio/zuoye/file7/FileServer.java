package com.yr.nio.zuoye.file7;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class FileServer {
	public static void main(String[] args) throws Exception {
		//打开服务器套接字通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//创建一个选择器
		Selector selector = Selector.open();
		//设置非阻塞模式
		serverSocketChannel.configureBlocking(false);
		//绑定监听端口
		serverSocketChannel.bind(new InetSocketAddress(6666));
		//注册选择器，保持等待模式
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		while(true){
			System.out.println("=============================" +selector.select());
			//返回此选择器的已选择键集
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterKey= keys.iterator();

			while(iterKey.hasNext()){
				SelectionKey sk = iterKey.next();
				iterKey.remove();
				//测试此键的通道是否已准备好接受新的套接字连接
				if(sk.isAcceptable()){
					System.out.println(1);
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					socketChannel.write(ByteBuffer.wrap("我是服务器发送给客户端的消息".getBytes()));
					socketChannel.register(selector, SelectionKey.OP_READ);

				} else if (sk.isReadable()) {
					FileOutputStream os = null;
					FileChannel fc = null;
					SocketChannel sc = (SocketChannel) sk.channel();
					System.out.println(4);
					try {

						// 文件名长度
						int nameLength = 0;
						// 文件名
						byte[] fileName = null;   // 如果文件以字节数组发来，我们就无从得知文件名，所以文件名一起发过来
						// 文件长度
						long contentLength = 0;
						// 文件内容
						byte[] contents = null;
						// 由于我们解析时前4个字节是文件名长度
						int capacity = 4;
						ByteBuffer buf = ByteBuffer.allocate(capacity);
						int size = 0;
						byte[] bytes = null;
						// 拿到文件名的长度
						size = sc.read(buf);
						System.err.println(size);
						if (size >= 0) {
							System.err.println(size);
							buf.flip();
							capacity = buf.getInt();
							buf.clear();
							nameLength = capacity;
						}


						bytes = null;
						// 拿文件名,相信文件名一次能够读完,如果你文件名超过1k
						buf = ByteBuffer.allocate(nameLength);
						while(true)
						{
							size = sc.read(buf);

							if (size >= nameLength) {
								buf.flip();
								bytes = new byte[nameLength];
								buf.get(bytes);
								fileName = bytes;
								buf.clear();
								break;
							}
						}

						// 拿到文件长度
						capacity = 8;
						buf = ByteBuffer.allocate(capacity);
						size = sc.read(buf);
						if (size >= 0) {
							buf.flip();
							// 文件长度是可要可不要的，如果你要做校验可以留下
							contentLength = buf.getLong();
							buf.clear();
						}

						// 用于接收buffer中的字节数组
						//ByteArrayOutputStream baos = new ByteArrayOutputStream();
						File file = new File("C:\\Users\\Administrator\\Desktop\\a.zip");
						os = new FileOutputStream(file);
						//获取通道
						fc = os.getChannel();
						// 文件可能会很大
						buf = ByteBuffer.allocate(1024*1024);
						int k = 0;
						size = sc.read(buf);
						while(size > 0){
							System.out.println("k=" + (k++) + " 读取到数据量:" + size);
							buf.flip();
//				            bytes = new byte[size];
							fc.write(buf);
							os.flush();
							buf.clear();
							size = sc.read(buf);
						} if(size == -1){
							System.out.println("fdfsaf");
							os.close();
							fc.close();
							sc.close();
						}
						System.out.println(5);
					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			}
		}
	}
}

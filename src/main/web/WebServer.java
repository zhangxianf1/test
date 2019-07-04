package main.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 代码建议：
1. 对于request，response这些类，最好先定义接口，明确好他们的职责，再实现类。
2. 代码里只考虑了200的情况，想想如果有404或者其他状态，这个代码要如何写，如何复用？如果有其他文本类型呢。
3. 考虑以后支持更多的api，比如访问文件内容、删除文件、更新文件等等，后台如何识别这些api？可了解下rest api

 */
/**
 * 服务端监听端口请求，并建立建立套接字，在线程中发送消息到客户端
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年6月28日
 */
public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);
	
//	http://localhost:8080/files?path=D:/succezIDE/workspace/study
	
	public static void main(String[] args) {
		//服务端套接字和客户端套接字
		ServerSocket server = null; 	
		Socket client = null;
		
		try {
			//监听指定端口
			server= new ServerSocket(8080);
			log.info("wait for brower to connect...");
			//使用套接字对象传送消息
			while(true) {
				client = server.accept();
				//在线程中发送信息
				new FileSystem(client).start();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}



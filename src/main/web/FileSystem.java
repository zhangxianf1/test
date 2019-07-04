package main.web;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.web.http.Request;
import main.web.http.Response;
import main.web.http.impl.HttpRequest;
import main.web.http.impl.HttpResponse;

/**
 * 根据客户端请求发送响应消息
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年7月3日
 */
public class FileSystem extends Thread {
	//日志对象
	private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	//响应消息和请求消息
	Request request;
	Response response;

	//构造request和response
	public FileSystem(Socket client) {
		try {
			request = new HttpRequest(client);
			response = new HttpResponse(client);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** 发送响应消息 */
	public void run() {
		try {
			log.debug("client: " + this.toString());
			log.debug("request: " + request.toString());
			log.debug("response: " + response.toString());
			
			//请求的资源路径
			String fileName = request.getServerPath();
			boolean isAjax = request.getHeader("Accept").equals("*/*");
			
			//返回响应消息
			response.sendResponse(fileName, isAjax);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

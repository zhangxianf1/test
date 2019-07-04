package main.web.http.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Enumeration;

import org.junit.experimental.theories.Theories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.web.http.Request;

/**
 * 提供请求的信息
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年7月3日
 */
public class HttpRequest implements Request {
	//日志对象
	private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	private Socket client; //客户端套接字
	private BufferedReader in;	//请求的输入流
	String header;		///请求的消息头

	public HttpRequest(Socket client) throws IOException {
		this.client = client;
		setReader();
	}

	/**
	 * 请求信息的头部
	 * @throws IOException */
	@Override
	public String getHeader() throws IOException {
		//如果之前读取过，就直接返回
		if(header != null) 
			return header;
		
		//使用StringBuuilder拼接读取到的header
		StringBuilder sb = new StringBuilder();
		String string = null;
		//读取到空行时停止
		while (!(string = in.readLine()).equals("")) {
			sb.append(string + "\n");
		}
		
		header = sb.toString();
		return sb.toString();
	}

	/**
	 * 根据请求头中的信息返回特定的值
	 * @param key 需要的信息名称
	 * @return 根据信息的名称返回值
	 * @throws IOException 
	 */
	@Override
	public String getHeader(String key) throws IOException {
		//如果header没有初始化，就进行读取
		if(header == null) 
			getHeader();
		//如果请求头中没有message，返回null
		if(header.indexOf(key) < 0) 
			return null;
		//根据message的下标和最近的一个换行符的下标获取值
		int first = header.indexOf(key) + key.length() + 2;
		int second = header.indexOf("\n", first);

		return header.substring(first, second);
	}

	/*请求的方法类型*/
	@Override
	public String getMethod() throws IOException {
		//请求行
		String msg = in.readLine();
		//请求行的开始到第一个空格之间就是请求方法
		return msg.substring(0, msg.indexOf(' '));
	}

	/*请求头部的资源文件路径*/
	@Override
	public String getServerPath() throws IOException {
		//获取请求行
		String msg = in.readLine();
		log.debug("request line: " + msg);

		//请求资源的相对路径：两个空格之间的字符串
		String fileName = msg.substring(msg.indexOf(' ') + 1);
		fileName = fileName.substring(1, fileName.indexOf(' '));
		
		return fileName;
	}

	/** 请求的输入流 
	 * @throws IOException */
	@Override
	public void setReader() throws IOException {
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}

	/** 根据参数名称获取参数值 */
	@Override
	public String getParameter(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}
}

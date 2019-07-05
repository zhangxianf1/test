package main.web.http.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.web.http.Response;
import main.web.util.FileUtil;


/**
 * 根据请求消息设置响应消息
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年7月3日
 */
public class HttpResponse implements Response {
	//日志对象
	private final Logger log = LoggerFactory.getLogger(HttpResponse.class);

	Socket client; //客户端套接字

	PrintStream out; //响应的输出流

	int status; //响应码

	String contentType; //文本类型及编码

	public HttpResponse(Socket client) throws IOException {
		this.client = client;
		setStream();
	}

	/** 根据套接字设置输出流 
	 * @throws IOException */
	@Override
	public void setStream() throws IOException {
		// TODO Auto-generated method stub
		out = new PrintStream(client.getOutputStream());
	}

	/** 根据请求消息设置响应码 */
	@Override
	public void setStatus(int status) {
		this.status = status;
	}
	
	

	/** 根据请求消息设置文本类型和编码*/
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/** 默认情况，显示文件内容 */
	public void sendResponse(String fileName){
		sendResponse(fileName, false);
	}

	/** 发送响应消息 */
	@Override
	public void sendResponse(String fileName, boolean isPost) {
		//如果请求的资源为空，返回hello world
		if (fileName.equals("") || fileName == null) {
			fileName = "index.html";
		}

		//根据文件名称设置文件对象
		File file = new File(fileName);
		if (!file.exists()) { //如果文件不存在，设置响应码，发送错误页面
			setStatus(404);
			sendError();
		}
		else { //如果请求的文件存在
			status = 200;
			if (file.isFile()) { //如果是文件，发送给客户端
				//根据文件名称设置contentType
				setContentType(fileName);

				log.info("{} start send.", fileName);
				//1.响应消息的状态行
				out.println("HTTP/1.0 " + status + " OK");
				//2.响应消息的文件类型
				if(isPost) {
					//设置contentType类型，自动下载文件
					out.println("Content-Type: application/octet-stream");
					//设置文件头
					out.println("Content-Disposition: attachment; filename=" + fileName.substring(fileName.indexOf('/') + 1));
				} else {
					//设置文件类型
					out.println("Content_Type:" + contentType + "; charset=UTF-8");
				}
				//3.响应消息的空行
				out.println("");
				//4.发送文件
				FileUtil.sendFile(out, file);
				//4.响应体数据
//				FileUtil.sendFile(out, file);
//				if(isAjax) {	//如果是ajax请求，就下载这个文件
//					//设置contentType类型，自动下载文件
//					out.println("Content-Type: application/octet-stream");
//					//设置文件头
//					out.println("Content-Disposition: attachment; filename=" + fileName.substring(fileName.indexOf('/') + 1));
//					out.println("");
//					//下载文件
//					FileUtil.sendFile(out, file);
//					
//				}  else {		//否则，就显示文件的内容
//					out.println("Content_Type:" + contentType + "; charset=UTF-8");
//					//3.响应消息的空行
//					out.println("");
//					//4.响应体数据
//					FileUtil.sendFile(out, file);
//				}
			}
			else { //如果是目录，则将目录的文件资源转换为json数据
				String res = FileUtil.getFiles(out, fileName);
				log.info("JSON data: " + res);
			}
		}

		//关闭客户端套接字
		try {
			client.close();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	
	/** 根据状态码返回错误消息*/
	@Override
	public void sendError() {
		// TODO Auto-generated method stub
		if (status == 404) {
			status = 200;
			sendResponse("webapp/404.html", false);
		}
	}



	/** 重定向到另一个页面 */
	public void sendRedirct(String fileName) {
	
	}
	
	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public int getStatus() {
		return status;
	}

}

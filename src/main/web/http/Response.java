package main.web.http;

import java.io.IOException;

/**
 * 响应请求，发送响应消息到客户端
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年7月3日
 */
public interface Response {
	/**根据状态码发送错误到客户端*/
	void sendError();
	
	//获取响应体的输出流
	void setStream() throws IOException;
	
	//1.根据请求的具体情况设置响应状态
	void setStatus(int status);
	
	//2.设置响应的文档类型和编码
	void setContentType(String contentType);
	
	//3.设置文件内容为响应体
	void sendResponse(String fileName, boolean isAjax);
}

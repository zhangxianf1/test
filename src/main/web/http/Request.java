package main.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Request接口，提供请求信息
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年7月3日
 */
public interface Request {
	/*请求消息的头部*/
	String getHeader() throws IOException;
	
	/** 在消息头中根据特定的名称返回特定的值 */
	String getHeader(String message) throws IOException;
	
	/*请求的资源部分：文件路径*/
	String getServerPath() throws IOException;
	
	/*请求使用的HTTP方法。*/
	String getMethod() throws IOException;
	
	/*获取请求体的输入流*/
	void setReader() throws IOException;
	
	/*根据参数名称获取请求的参数值*/
	String getParameter(String parameter);
}
	

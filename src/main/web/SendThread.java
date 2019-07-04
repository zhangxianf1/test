package main.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

/**
 * 服务端发送信息到客户端的线程
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author Administraor
 * @createdate 2019年6月28日
 */
@Deprecated
public class SendThread extends Thread {
	//客户端套接字
	private Socket client;

	//日志对象
	private final Logger log = LoggerFactory.getLogger(SendThread.class);

	public SendThread(Socket client) {
		this.client = client;
	}

	/**
	 * 设置响应报文发送给客户端
	 */
	public void run() {
//		//获取客户端的ip和端口
//		String clientIp = client.getInetAddress().toString();
//		int clientPort = client.getPort();

		try {
			//在客户端套接字上建立输入输出流
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream out = new PrintStream(client.getOutputStream());

			//获取浏览器请求中的请求头
			String msg = in.readLine();
			//获取请求头中的请求的文件资源名
			String fileName = getResourcePath(msg);

			//如果请求的资源为空，返回hello world
			if (fileName.equals("") || fileName == null) {
				//设置响应报文
				out.println("HTTP/1.0 200 OK");
//				out.println("MIME_version:1.0");
				out.println("Content_Type:text/html; charset=UTF-8");
				out.println("Content_Length: 11");
				out.println("");//报文头和信息之间要空一行
				out.println("hello world");
			}
			else { //如果请求的资源不为空
				log.info("request resource: {}", fileName);
				File file = new File(fileName);
				if (file.exists()) { //如果文件存在
					if (file.isFile()) { //如果是文件，传送或下载
						//设置响应报文
						log.info("{} start send.", fileName);

						//设置响应报文
						out.println("HTTP/1.0 200 OK");
//						out.println("MIME_version:1.0");
						//根据文件后缀名设置Content_Type
						String type = fileName.substring(fileName.indexOf('.') + 1);
						if (type.equals("js"))
							type = "javascript";
						out.println("Content_Type:text/" + type + "; charset=UTF-8");
						int len = (int) file.length();

						out.println("Content_Length:" + len);
						out.println("");//报文头和信息之间要空一行
						//响应体发送的数据
						sendFile(out, file);
					} else {	//如果是目录，则将目录的文件资源转换为json数据
						String res = getFiles(fileName);
						log.debug(res);
					}
				}

				//清空缓存
//				out.flush();
			}

			client.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 根据url中的路径获取本地文件资源，返回json字符串
	 * @param filePath
	 */
	private String getFiles(String filePath) {
		//当前项目路径的文件对象
		File file = new File(filePath);
		//文件数组
		File[] fs = file.listFiles();

		//将文件的数据放入数组中：名称，长度，日期
		String[][] files = new String[fs.length][3];

		//遍历目录中的每一个文件
		for (int i = 0; i < fs.length; i++) {
			/** 1.文件的名称 */
			files[i][0] = fs[i].getName();
			/** 2. 文件的大小 */
			if (fs[i].isDirectory()) { //目录无法计算大小
				files[i][1] = "";
			}
			else { //计算文件的大小
				long length = fs[i].length();
				//计算文件的单位
				String[] unit = { "B", "KB", "MB", "GB" };
				int j = 0;
				//将文件大小循环除以1024，再加上对应的单位
				while (length / 1024.0 > 1) {
					length /= 1024.0;
					j++;
				}
				//文件的大小保留两位小数
				DecimalFormat df = new DecimalFormat("0.00");
				files[i][1] = df.format(length) + unit[j];
			}
			/** 3.文件的最后修改日期 */
			//定义日期格式
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			//根据文件的最后修改时间来得到格式化的时间
			files[i][2] = df.format(
					LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.of("Asia/Shanghai")));
		}

		//把对象集合转换为字符串
		String result = JSONArray.fromObject(files).toString();
		//把json字符串输出到流中
		try {
			PrintStream out = new PrintStream(client.getOutputStream());
			out.println("HTTP/1.0 200 OK");
			out.println("Content_Type:text/html; charset=UTF-8");
			out.println("");
			out.println(result);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 服务端发送文件到客户端
	 * @param out 输出流
	 * @param file 文件资源
	 */
	public void sendFile(PrintStream out, File file) {
		try {
			//读取文内容到buf数组中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int len = (int) file.length();
			byte buf[] = new byte[len];
			in.readFully(buf);
			//将buf数组中的内容写入到流中
			out.write(buf, 0, len);
			out.flush();
			in.close();
		}
		catch (Exception e) {
			log.error(e.getMessage());
			//			System.exit(1);
		}
	}

	/**
	 * 从请求报文的消息头中获取请求的资源文件路径
	 * @param s 请求报文
	 * @return 消息头中的资源文件路径
	 */
	private String getResourcePath(String msg) {
//		if(msg == null) return null;
		//截取请求头，获取请求资源
		String res = msg.substring(msg.indexOf(' ') + 1);
		res = res.substring(1, res.indexOf(' '));
		return res;
	}

}

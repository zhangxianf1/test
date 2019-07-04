package main.web.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.web.http.Response;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import net.sf.json.JSONArray;

public class FileUtil {
	//日志对象
	private final static Logger log = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 根据url中的路径获取本地文件资源，返回json字符串
	 * @param filePath
	 */
	public static String getFiles(PrintStream out, String filePath) {
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
				DecimalFormat df = new DecimalFormat("0.0");
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
		out.println("HTTP/1.0 200 OK");
		out.println("Content_Type:text/html; charset=UTF-8");
		out.println("");
		out.println(result);

		return result;
	}
	
	/**
	 * 服务端发送文件到客户端
	 * @param out 输出流
	 * @param file 文件资源
	 */
	public static void sendFile(PrintStream out, File file) {
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
	 * 根据文件名称返回对应的contentType
	 * @param fileName 文件名称
	 * @return	对应的contentType
	 */
	public static String contentType(String fileName) {
		//文件对象
		Path path = Paths.get(fileName);
		String contentType = null;
		try {
			//检测文件的MIME类型
			contentType = Files.probeContentType(path);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		//返回MIME类型
		return contentType;
	}
}

package main.basic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import test.basic.TestFile2Buf;

public class File2Buf {
	/**
	 * 将文件内容转换成byte数组返回,如果文件不存在或者读入错误返回null
	 */
	public static byte[] file2buf(File fobj) {
		//文件为空，则返回null
		if (fobj == null)
			return null;
		
		//获取文件的字节长度
		long length = fobj.length();
		//根据文件长度创建读取文件的byte数组，读取的最大字节数为int的最大值
		byte[] fileBytes = new byte[(int) length];

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			//建立字节输入流和缓冲流
			fis = new FileInputStream(fobj);
			bis = new BufferedInputStream(fis);
			//读取文件到byte数组
			while (bis.read(fileBytes) != -1) {
			}
		}
		catch (FileNotFoundException e) {
			System.err.println("文件不存在");
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			System.err.println("文件读取错误");
			e.printStackTrace();
			return null;
		}
		finally {	//关闭字节流和缓冲流
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
				if (bis != null) {
					bis.close();
					bis = null;
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("文件流关闭失败");
				e.printStackTrace();
			}

		}
		return fileBytes;
	}

}

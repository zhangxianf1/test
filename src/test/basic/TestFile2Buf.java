package test.basic;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import main.basic.File2Buf;

public class TestFile2Buf {
	//使用类的相对路径读取文件
	File file = new File(TestFile2Buf.class.getResource("input.txt").getFile());

	/**
	 * 测试：使用I/O流读取文件的字节数组的内容和长度是否和预期一致
	 */
	@Test
	public void test1() {
		//直接从input.txt中的字符串内容获取字节数组
		byte[] expectedBytes = "11\n中文\n".getBytes();
		//使用I/O流获取input.txt中的所有字节
		byte[] actualBytes = File2Buf.file2buf(file);

		//判断字节数组的内容是否相同
		assertArrayEquals(expectedBytes, actualBytes);
		//判断字节数组的长度
		assertEquals(10, actualBytes.length);
	}

}

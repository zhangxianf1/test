package test.basic;

import static org.junit.Assert.*;

import org.junit.Test;

import main.basic.IntToHex;

public class TestIntToHex {
	/**
	 * 测试：整数的16进制表示是否和预期的一致
	 */
	@Test
	public void test1() {
		assertEquals("0", IntToHex.intToHex(0));
		assertEquals("7FFFFFFF", IntToHex.intToHex(Integer.MAX_VALUE));
		assertEquals("-7FFFFFFF", IntToHex.intToHex(-Integer.MAX_VALUE));
	}
}

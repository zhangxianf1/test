package main.basic;

public class IntToHex {
	/**
	 * 要求：将一个整数转换为16进制的字符串
	 * 方法：用这个整数除以16并对16取模，直到商为0，期间产生的模是需要的结果。
	 */
	public static String intToHex(int num) {
		//后面的方法只适用于正数，因此要保留负数的符号
		String symbol = "";
		//判断num的值
		if (num == 0)
			return "0";
		else if (num < 0)
			symbol = "-";

		//res表示商，mod表示模，sb存储最终结果
		int res = Math.abs(num);
		int mod = 0;
		StringBuilder sb = new StringBuilder();

		//除以16直到商为0
		while (res != 0) {
			//计算商和模，商就是下一次的被除数
			mod = res % 16;
			res = res / 16;
			//模需要头插入到数组中
			if (mod > 9) { //如果模大于9，需要转换成相应的字母字符
				sb.insert(0, (char) (mod - 10 + (int) ('A')));
			}
			else { //如果模不大于9，直接转换成相应的数字字符
				sb.insert(0, (char) (mod + (int) ('0')));
			}
		}

		return symbol + sb.toString();
	}

}

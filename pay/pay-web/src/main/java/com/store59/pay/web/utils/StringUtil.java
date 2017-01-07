package com.store59.pay.web.utils;

public class StringUtil {
	public static String formatMoney(int amount) {
		String preAmountStr = String.valueOf(amount);
		int zeroLen = 15 - preAmountStr.length();
		StringBuffer zeroStr = new StringBuffer();
		while(zeroLen > 0) {
			zeroStr.append("0");
			zeroLen--;
		}
		return zeroStr.append(preAmountStr).toString(); 
	}
}

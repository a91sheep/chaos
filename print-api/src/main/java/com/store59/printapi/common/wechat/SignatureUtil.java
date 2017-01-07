package com.store59.printapi.common.wechat;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class SignatureUtil {
	/**
	 * 生成事件消息接收签名
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String generateEventMessageSignature(String token, String timestamp,String nonce) {
		String[] array = new String[]{token,timestamp,nonce};
		Arrays.sort(array);
		String s = StringUtils.arrayToDelimitedString(array, "");
		return DigestUtils.shaHex(s);
	}

}
package com.techwells.teammission.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
	private static final String SALT="加盐";  //加盐
	
	/**
	 * 生成消息摘要
	 * @param value
	 * @return
	 */
	public static String getMd5(String value){
		return DigestUtils.md5Hex(SALT+value);
	}
}

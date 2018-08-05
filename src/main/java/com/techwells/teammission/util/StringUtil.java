package com.techwells.teammission.util;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringUtil {
	
	/**
	 * 将字符串数组转换成set集合
	 * @param array  待转换的数组
	 * @return  {@link Set<Object>}
	 * @throws Exception
	 */
	public static Set<Object> convertStringToCollection(String[] array)throws Exception{
		Set<Object> set=new HashSet<Object>(array.length);
		for (String str : array) {
			set.add(str);
		}
		return set;
	}
	
	
	public static String getRedisKey(String domainKey,Map<String, Object> params){
		StringBuilder builder=new StringBuilder();
		builder.append(domainKey+"_");
		for (String key : params.keySet()) {
			builder.append(key+"="+params.get(key)+"_");
		}
		return builder.toString();
	}
}

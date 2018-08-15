package com.techwells.teammission.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
	private static final String SALT="加盐"; 
	
	/**
	 * 返回指定字符串的消息摘要
	 * @param value
	 * @return
	 */
	public static String getMd5(String value){
		return DigestUtils.md5Hex(SALT+value);
	}
	
	/**
	 * 使用原生的md5加密算法获取图片的消息摘要
	 * @param imagePath 图片的路径
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
    public static String getMD5(String imagePath) throws NoSuchAlgorithmException, IOException {
        InputStream in = new FileInputStream(new File(imagePath));
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];
 
        int len = 0;
        while ((len = in.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, len);   //更新摘要
        }
 
        byte[] mdbytes = md.digest();   //生成最终的消息摘要
 
        // convert the byte to hex format
        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString().toLowerCase();
    }
    
    /**
     * 使用工具类org.apache.commons.codec.digest.DigestUtils生成消息摘要
     * @param stream   文件的输入流
     * @return  十六进制的消息摘要
     * @throws IOException
     */
    public String getMD5(InputStream stream) throws IOException{
    	return DigestUtils.md5Hex(stream);
    }
}

package com.techwells.teammission.domain.rs;

import java.io.Serializable;

/**
 * 项目动态，用户，文件的值类
 * @author Administrator
 *
 */
public class DynamicUserFileVos implements Serializable {
	private static final long serialVersionUID = -5800495647472321915L;
	
	private String userName;  //用户名，这里是用户的真实姓名
	private String userIcon;   //用户头像
	private String createDate;  //发布时间
	private String fileName;   //文件名称
	private String content;   //动态的内容
	
	
}

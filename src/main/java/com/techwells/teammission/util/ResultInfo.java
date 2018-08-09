package com.techwells.teammission.util;

import java.util.HashMap;

/**
 * 返回信息
 */
public class ResultInfo {
	private String code = "200";   //状态码   200 表示成功
	private String message = "";    //提示信息
	private int total = 0;   //查询到的总数
	private Object result;  //封装结果集
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	

}

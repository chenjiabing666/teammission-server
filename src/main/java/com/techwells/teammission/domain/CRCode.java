package com.techwells.teammission.domain;

import java.util.Date;

/**
 * 验证码的封装类
 * @author Administrator
 *
 */
public class CRCode {
	private String crCode;   //验证码
	private String mobile;  //手机号码
	private Date sendDate;  //发送日期
	private int number=0;  //发送短信的条数
	public String getCrCode() {
		return crCode;
	}
	public void setCrCode(String crCode) {
		this.crCode = crCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
}
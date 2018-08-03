package com.techwells.teammission.util;

/**
 * 
 * 
 * <p>@ClassName: ResultErrorInfo.java</p> 
 * <p>
 * Description: ���ش�����Ϣ
 * </p> 
 * @author Rocky 
 * @date 2015-7-24 ����3:24:52 
 * @version 1.0
 */
public class ResultErrorInfo {
	private String errorCode;   //状态码
	private String errorMessage;   //错误信息
	public ResultErrorInfo() {
		super();
	}

	public ResultErrorInfo(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

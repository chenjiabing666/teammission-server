package com.techwells.teammission.util;

import java.util.HashMap;

/**
 * 
 * 
 * <p>
 * 
 * @ClassName: ResultSuccessInfo.java
 *             </p>
 *             <p>
 *             Description: ���سɹ���Ϣ
 *             </p>
 * 
 * @author Rocky
 * @date 2015-7-24 ����3:24:52
 * @version 1.0
 */
public class ResultSuccessInfo {
	private String successCode = "0";   //状态码
	private String successMessage = "";    //提示信息
	private int total = 0;   //查询到的总数
	private double totalIncome;   
	private double totalExpenditure;
	private Object result;  //封装结果集

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(double totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}


	public ResultSuccessInfo() {

	}

	public ResultSuccessInfo(String code) {
		super();
		this.successCode = code;
	}

	public ResultSuccessInfo(Object result) {
		super();
		this.result = result;
	}

	public ResultSuccessInfo(String errorCode, String errorMessage,
			Object result) {
		super();
		this.successCode = errorCode;
		this.successMessage = errorMessage;
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		if (result == null) {
			this.result = new HashMap<Object, Object>();
			return;
		}
		this.result = result;
	}

}

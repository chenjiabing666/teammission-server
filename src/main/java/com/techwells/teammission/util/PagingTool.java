package com.techwells.teammission.util;

import java.util.HashMap;
import java.util.Map;



public class PagingTool {

	private int startNum = 0;   //开始的偏移量
	private int pageSize = 10;   //默认的每页显示的条数为10条
	private Map<String, Object> params = new HashMap<String, Object>();   //封装过滤条件
	
	//默认的构造方法
	public PagingTool() {

	}
	
	
	/**
	 * @param pageNum 当前页数  
	 * @param pageSize   每页显示多少条
	 */
	public PagingTool(Integer pageNum, Integer pageSize) {
		this.startNum = (pageNum-1)*pageSize;    //计算偏移量
		this.pageSize = pageSize;  
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		if(startNum != null){
			this.startNum = startNum;
		}else{
			System.err.print("pageNum is null");
		}
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if(pageSize != null){
			this.pageSize = pageSize;
		}else{
			System.err.print("pageSize is null");
		}
	}


	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	

	@Override
	public String toString() {
		return "PagingTool [startNum=" + startNum
				+ ", pageSize=" + pageSize
				+ ", params=" + params + "]";
	}
	
}

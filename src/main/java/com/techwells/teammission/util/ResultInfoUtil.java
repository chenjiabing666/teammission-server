package com.techwells.teammission.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * <p>
 * 
 * @ClassName: ResultInfoUtil.java
 *             </p>
 *             <p>
 *             Description:
 *             </p>
 * 
 * @author Rocky
 * @date 2015-7-24 ����3:24:52
 * @version 1.0
 */
public class ResultInfoUtil {

	private static final String SUCCESS = "0";
	public static final String ERROR = "400";

	private static Map<String, String> errorMap = new HashMap<String, String>();
	static {
		errorMap.put(SUCCESS, "返回成功！");
		errorMap.put(ERROR, "返回失败!");
	}

	/***
	 * 
	 * @param obj
	 * @return
	 */
	public static ResultSuccessInfo setSuccessInfo(Object obj) {
		ResultSuccessInfo resultSuccessInfo = new ResultSuccessInfo();
		resultSuccessInfo.setSuccessCode(SUCCESS);
		resultSuccessInfo.setResult(obj);
		return resultSuccessInfo;
	}

	/***
	 * 
	 * @param type
	 * @return
	 */
	public static ResultErrorInfo setErrorInfo(String type) {
		ResultErrorInfo resultInfo = new ResultErrorInfo();
		resultInfo.setErrorMessage(ResultInfoUtil.errorMap.get(type));
		resultInfo.setErrorCode(type);
		return resultInfo;
	}

}

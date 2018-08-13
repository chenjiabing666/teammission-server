package com.techwells.teammission.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.techwells.teammission.service.ProjectDynamicService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

/**
 * 项目动态的controller
 * @author Administrator
 *
 */
@RestController
public class ProjectDynamicController {
	@Resource
	private ProjectDynamicService dynamicService;
	
	
	@RequestMapping("/dynamic/getDynamic")
	public Object getDynamic(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		
		String projectId=request.getParameter("projectId");   //项目Id
		String num=request.getParameter("num");   //动态数量
		
		//校验参数
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(num)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("动态数量不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(projectId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("项目Id只能为数字");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(num)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("动态数量只能为数字");
			return resultInfo;
		}
		
		
		try {
			Object object=dynamicService.getDynamic(Integer.parseInt(projectId), Integer.parseInt(num));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("动态获取异常");
			return resultInfo;
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

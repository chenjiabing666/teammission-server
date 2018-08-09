package com.techwells.teammission.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.ProjectService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

/**
 * 项目的controller
 * @author Administrator
 *
 */
@RestController
public class ProjectController {
	
	@Resource
	private ProjectService projectService;
	
	/**
	 * 创建项目  
	 * @param userId : 用户id
	 * @param projectName 项目名称
	 * @param introduction 项目简介
	 * @param request
	 * @return
	 */
	@RequestMapping("/project/createProject")
	public Object createProject(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		
		//获取请求参数
		String userId=request.getParameter("userId");
		String projectName=request.getParameter("projectName");
		String introduction=request.getParameter("introduction");
		
		
		//请求参数校验
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("1000010");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(projectName)) {
			resultInfo.setCode("1000011");
			resultInfo.setMessage("项目名称不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(introduction)) {
			resultInfo.setCode("1000012");
			resultInfo.setMessage("项目简介不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(userId)) {
			resultInfo.setCode("1000013");
			resultInfo.setMessage("用户Id只能是数字");
			return resultInfo;
		}
		
		try {
			Object object=projectService.createNewProject(Integer.parseInt(userId), projectName, introduction);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("1000014");
			resultInfo.setMessage("创建文件异常");
			return resultInfo;
		}
		
	}
	
	/**
	 * 获取指定项目的项目成员信息
	 * @param projectId  项目Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/project/getUserList")
	public Object getUserListByProjectId(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String projectId=request.getParameter("projectId");
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(projectId)) {
			resultInfo.setCode("100002");
			resultInfo.setMessage("项目Id只能是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=projectService.getProjectUserByProjectId(Integer.parseInt(projectId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100003");
			resultInfo.setMessage("获取成员信息异常");
			return resultInfo;
		}
		
	}
	
	
	/**
	 * 移交项目
	 * @param userId 项目拥有者Id
	 * @param projectId 项目Id
	 * @param toUserId  项目成员Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/project/transferProject")
	public Object transferProject(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String userId=request.getParameter("userId");
		String projectId=request.getParameter("projectId");
		String toUserId=request.getParameter("toUserId");
		
		//校验字段
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("1000011");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
			
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("1000012");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(toUserId)) {
			resultInfo.setCode("1000013");
			resultInfo.setMessage("项目成员Id不能为空");
			return resultInfo;
		}
		
		try {
			Object object=projectService.transferProject(Integer.parseInt(projectId), Integer.parseInt(userId), Integer.parseInt(toUserId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("移交异常");
			return resultInfo;
		}
	}
	
	
	
	/**
	 * 获取项目的详细信息，包括项目拥有者姓名，公开性
	 * @param projectId  项目Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/project/getProjectInfo")
	public Object getProjectInfo(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String projectId=request.getParameter("projectId");
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100010");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(projectId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("项目Id只能是数字");
			return resultInfo;
		}
		
		
		
		try {
			Object object=projectService.getProjectInfoById(Integer.parseInt(projectId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("1000013");
			resultInfo.setMessage("获取项目详细信息异常");
			return resultInfo;
		}
		
	}
	
	
	@RequestMapping("/project/modifyProjectInfo")
	public Object modifyProjectInfo(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		
		//获取请求参数
		String projectId=request.getParameter("projectId");
		String projectName=request.getParameter("projectName");
		String introduction=request.getParameter("introduction");
		String publicity=request.getParameter("publicity");
		
		
		//检验参数
		if (StringUtils.isEmpty(projectId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("项目Id不能为空");
			return resultInfo;
		}
		

		if (StringUtils.isEmpty(projectName)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("项目名称不能为空");
			return resultInfo;
		}
		

		if (StringUtils.isEmpty(introduction)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("项目简介不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(publicity)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("项目公开性不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(publicity)) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("项目公开性必须是数字");
			return resultInfo;
		}
		
		try {
			Object object=projectService.modifyProjectInfo(Integer.parseInt(projectId), projectName, introduction, Integer.parseInt(publicity));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("修改异常");
			return resultInfo;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

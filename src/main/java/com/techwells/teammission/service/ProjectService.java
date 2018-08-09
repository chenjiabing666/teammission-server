package com.techwells.teammission.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techwells.teammission.domain.Project;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.util.PagingTool;

@Transactional
public interface ProjectService {
	
	/**
	 * 用户新建一个项目
	 * @param userId  用户Id
	 * @param projectName  项目名称
	 * @param introduction  项目简介
	 * @return  返回结果集
	 * @throws Exception  抛出异常
	 */
	Object createNewProject(Integer userId,String projectName,String introduction)throws Exception;  
	
	
	/**
	 * 根据项目Id获取成员列表
	 * @param projectId  项目Id
	 * @return
	 */
	Object getProjectUserByProjectId(Integer projectId)throws Exception;
	
	
	/**
	 * 移交项目
	 * @param ProjectId  项目Id
	 * @param userId     用户Id
	 * @param toUserId   移交对方的Id
	 * @return
	 */
	Object transferProject(Integer projectId,Integer userId,Integer toUserId)throws Exception;
	
	/**
	 * 根据项目Id获取该项目的详细信息
	 * @param projectId  项目Id
	 * @return
	 */
	Object getProjectInfoById(Integer projectId)throws Exception;
	
	/**
	 * 修改项目信息
	 * @param projectId 项目Id
	 * @param projectName  项目名称
	 * @param introduction  简介
	 * @param publicity   公开性
	 * @return  
	 * @throws Exception
	 */
	Object modifyProjectInfo(Integer projectId,String projectName,String introduction,Integer publicity) throws Exception;
	
	
}

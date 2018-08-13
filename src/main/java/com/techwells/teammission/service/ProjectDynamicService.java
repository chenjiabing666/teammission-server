package com.techwells.teammission.service;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.ProjectDynamic;

/**
 * 项目动态
 * @author Administrator
 *
 */
@Transactional
public interface ProjectDynamicService {
	
	
	/**
	 * 获取项目的动态
	 * @param projectId
	 * @param number
	 * @return
	 * @throws Exception
	 */
	Object getDynamic(Integer projectId,int number)throws Exception;
	
	/**
	 * 添加项目动态
	 * @param dynamic 动态对象
	 * @return
	 * @throws Exception
	 */
	Object addDynamic(ProjectDynamic dynamic)throws Exception;
	
}

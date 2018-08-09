package com.techwells.teammission.service.impl;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import sun.util.logging.resources.logging;

import com.sun.crypto.provider.RSACipher;
import com.techwells.teammission.dao.ProjectMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.dao.UserProjectMapper;
import com.techwells.teammission.domain.Project;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.domain.UserProject;
import com.techwells.teammission.domain.UserProjectKey;
import com.techwells.teammission.domain.rs.ProjectUserVos;
import com.techwells.teammission.service.BaseServiceUtils;
import com.techwells.teammission.service.ProjectService;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.PagingTool;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	private Logger logger=Logger.getLogger(ProjectServiceImpl.class);
	
	@Resource
	private RedisUtils redisUtils;
	
	@Resource
	private ProjectMapper projectMapper;
	
	@Resource
	private UserProjectMapper userProjectMapper;
	
	
	@Value("#{redisParameter.projectHashKey}")
	private String PROJECT_KEY;     //项目在缓存中对应的key
	
	@Value("#{redisParameter.projectExpireTime}")
	private Long PROJECT_EXPIRE_TIME;      //项目缓存的过期时间
	
	@Value("#{redisParameter.userProjectHashKey}")
	private String UESER_PROJECT;
	
	@Value("#{redisParameter.useProjectExpireTime}")
	private Long USER_PROJECT_EXPIRE_TIME;  
	
	//缓存每个项目对应成员的key
	@Value("#{redisParameter.projectMemberHashKey}")
	private String PROJECT_MEMBERS;
	
	//缓存项目成员的过期时间
	@Value("#{redisParameter.projectMemberExpireTime}")
	private Long PROJECT_MEMBERS_EXPIRE_TIME;


	@Override
	public Object createNewProject(Integer userId, String projectName,
			String introduction) throws Exception {
		
		ResultInfo resultInfo=new ResultInfo();  //结果集
		
		ProjectWithBLOBs project=new ProjectWithBLOBs();  //新建一个项目
		project.setProjectName(projectName);  //项目名称
		project.setIntroduction(introduction);  //简介
		project.setCreatedDate(new Date());  //创建时间
		//其他的参数是数据库默认的，公开性，完成状态
		
		int count=projectMapper.insertSelective(project);   //向数据库中添加数据
		
		//如果没有添加成功，直接返回
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("添加失败");
			return resultInfo;
		}
		
		
		try {
			//添加到缓存中
			redisUtils.addHashObject(PROJECT_KEY, project.getProjectId()+"", project);
		} catch (Exception e) {
			throw new RuntimeException();  //回滚数据
		}
		
		
		UserProject userProject=new UserProject();  //新建用户和项目的关联对象
		userProject.setUserId(userId);
		userProject.setCreatedDate(new Date());
		userProject.setProjectId(project.getProjectId());
		
		int count1=userProjectMapper.insertSelective(userProject);  //绑定项目
		
		if (count1==0) {
			resultInfo.setCode("100002");
			resultInfo.setMessage("添加失败");
			return resultInfo;
		}
		
		resultInfo.setMessage("添加成功");
		return resultInfo;
	}



	//获取每个项目的成员信息，使用hash类型，缓存在redis中
	@Override
	public Object getProjectUserByProjectId(Integer projectId)throws Exception{
		ResultInfo resultInfo=new ResultInfo();
		
		List<User> users=null;
		
		//从缓存中取出数据
		try {
			//从缓存中获取数据
			users=(List<User>) redisUtils.getHashObject(PROJECT_MEMBERS, projectId+"");
//			users=(List<User>) redisUtils.getStringObject(UESER_PROJECT+"_"+projectId);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//如果从缓存中成功获取
		if (users!=null) {
			logger.debug("---------------------从缓存中获取成员列表成功--------------------");
			resultInfo.setMessage("获取成功");
			resultInfo.setResult(users);
			return resultInfo;
		}
		
		
		//缓存中没有获取到，那么从数据库中查找
		users=projectMapper.selectUserByProjectId(projectId);  //从数据库中获取信息
		
		//添加到缓存中
		try {
			//将数据缓存在hash中
			redisUtils.addHashObject(PROJECT_MEMBERS,projectId+"" , users,PROJECT_EXPIRE_TIME,TimeUnit.SECONDS);
//			redisUtils.addStringObject(UESER_PROJECT+"_"+projectId, users, USER_PROJECT_EXPIRE_TIME, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("获取成功");
		resultInfo.setResult(users);
		return resultInfo;
		
	}



	
	
	/**
	 * 移交项目
	 * 	只要改变一个power字段即可
	 */
	@Override
	public Object transferProject(Integer projectId, Integer userId,
			Integer toUserId) {
		ResultInfo resultInfo=new ResultInfo();
		
		UserProject userProject1=new UserProject();
		userProject1.setUserId(userId);
		userProject1.setProjectId(projectId);
		userProject1.setPower(0);  //修改为成员
		int count=userProjectMapper.updateByPrimaryKeySelective(userProject1);   //修改权限
		
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("移交失败");
			return resultInfo;
		}
		
		UserProject userProject2=new UserProject();
		userProject2.setUserId(toUserId);
		userProject2.setProjectId(projectId);
		userProject2.setPower(1);  //修改为项目拥有者
		int count1=userProjectMapper.updateByPrimaryKeySelective(userProject2);
		
		if (count1==0) {
			resultInfo.setCode("100002");
			resultInfo.setMessage("移交失败");
			return resultInfo;
		}
		
		resultInfo.setMessage("修改成功");
		return resultInfo;
		
	}



	
	
	@Override
	public Object getProjectInfoById(Integer projectId) {
		ResultInfo resultInfo=new ResultInfo();
		ProjectUserVos projectUserVos=userProjectMapper.selectProjectUserVosByProjectId(projectId);
		
		if (projectUserVos==null) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("该项目信息不存在");
			return resultInfo;
		}
		
		resultInfo.setResult(projectUserVos);
		resultInfo.setMessage("获取成功");
		return resultInfo;
	}



	@Override
	public Object modifyProjectInfo(Integer projectId, String projectName,
			String introduction, Integer publicity) throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		ProjectWithBLOBs projectWithBLOBs=projectMapper.selectByPrimaryKey(projectId);   //根据Id获取项目的详细信息
		
		if (projectWithBLOBs==null) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("该项目信息不存在");
			return resultInfo;
		}
		
		
		projectWithBLOBs.setProjectName(projectName);
		projectWithBLOBs.setIntroduction(introduction);
		projectWithBLOBs.setPublicity(publicity);
		
		
		int count=projectMapper.updateByPrimaryKeySelective(projectWithBLOBs);
		
		//修改失败
		if(count==0){
			resultInfo.setCode("100002");
			resultInfo.setMessage("修改失败");
			return resultInfo;
		}
		
		
		//修改缓存中的数据
		try {
			redisUtils.addHashObject(PROJECT_KEY, projectId+"", projectWithBLOBs);
		} catch (Exception e) {
			throw new Exception();
		}
		
		resultInfo.setMessage("修改成功");
		return resultInfo;
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

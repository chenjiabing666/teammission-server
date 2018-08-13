package com.techwells.teammission.service.impl;

import java.util.List;
import java.util.Stack;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.techwells.teammission.dao.ProjectDynamicMapper;
import com.techwells.teammission.domain.ProjectDynamic;
import com.techwells.teammission.service.ProjectDynamicService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class ProjectDynamicServiceImpl implements ProjectDynamicService {
	@Resource
	private ProjectDynamicMapper dynamicMapper;
	
	@Resource
	private RedisUtils redisUtils;
	
	@Value("#{redisParameter.projectDynamicListKey}")
	private String PROJECT_DYNAMIC;      //键值key
	
	@Value("#{redisParameter.projectDynamicExpireTime}")
	private String PROJECT_DYNAMIC_EXPIRE_TIME;   //过期时间

	

	@Override
	public Object getDynamic(Integer projectId,int number)throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		//从缓存中获取
		List<Object> dynamics=null;
		
		try {
			dynamics=redisUtils.getRangeListObject(PROJECT_DYNAMIC+projectId, 0, number);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//缓存中存在这个数据
		if (dynamics!=null) {
			resultInfo.setMessage("成功获取动态");
			resultInfo.setResult(dynamics);
			resultInfo.setTotal(dynamics.size());
			return resultInfo;
		}
		
		//如果不存在，那么从数据库中获取
		List<ProjectDynamic> projectDynamics=dynamicMapper.selectDynamicsByProjectId(projectId, number);
		
		
		//如果查到数据了，那么添加缓存中
		if(projectDynamics!=null){
			try {
				//添加到缓存中
				redisUtils.addRigthListObjectBatch(PROJECT_DYNAMIC+projectId, redisUtils.convertToCollection(projectDynamics), null, null);
			} catch (Exception e) {
				throw new RuntimeException();
			}
			
			resultInfo.setMessage("获取成功");
			resultInfo.setResult(projectDynamics);
			resultInfo.setTotal(projectDynamics.size());
			return resultInfo;
		}
		
		resultInfo.setMessage("获取成功");
		resultInfo.setResult(projectDynamics);
		resultInfo.setTotal(0);
		return resultInfo;
	}

	
	@Override
	public Object addDynamic(ProjectDynamic dynamic) throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		
		//添加数据到mysql中
		int count=dynamicMapper.insertSelective(dynamic);  
		
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("添加失败");
			return resultInfo;
		}
		
		//如果添加成功，将数据添加到redis中
		try {
			//key是projectDynameic_projectId
			redisUtils.addLeftListObject(PROJECT_DYNAMIC+dynamic.getProjectId(),dynamic);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		resultInfo.setMessage("添加成功");
		return resultInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

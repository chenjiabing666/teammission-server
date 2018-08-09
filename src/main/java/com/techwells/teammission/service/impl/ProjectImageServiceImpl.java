package com.techwells.teammission.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.techwells.teammission.dao.ProjectImageMapper;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.service.ProjectImageService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class ProjectImageServiceImpl implements ProjectImageService {
	
	@Resource
	private ProjectImageMapper projectImageMapper;
	
	@Resource
	private RedisUtils redisutils;
	
	@Value("#{redisParameter.projectImageHashKey}")
	private String PROJECT_IMAGE;
	
	@Value("#{redisParameter.projectImageExpireTime}")
	private String PROJECT_IMAGE_EXPIRE_TIME;
	
	
	@Override
	public Object uploadProjectImageBatch(List<ProjectImage> projectImages)throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		
		if (projectImages.size()==0) {
			resultInfo.setCode("100002");
			resultInfo.setMessage("请选择图片");
			return resultInfo;
		}
		//添加数据到数据库中
		int count=projectImageMapper.insertImageBatch(projectImages);
		
		//数量不相同，那么上传失败
		if (count<projectImages.size()) {
			throw new RuntimeException();  //直接抛异常，回滚数据
		}
		
		//上传成功，添加缓存中
		try {
			redisutils.addHashObject(PROJECT_IMAGE,projectImages.get(0).getProjectId()+"", projectImages);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("上传成功");
		return resultInfo;
	}
	
}

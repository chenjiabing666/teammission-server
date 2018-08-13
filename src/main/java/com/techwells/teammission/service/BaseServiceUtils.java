package com.techwells.teammission.service;

import java.util.List;

import com.techwells.teammission.dao.ProjectDynamicMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.ProjectDynamic;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.util.RedisUtils;

/**
 * service的工具类
 * @author Administrator
 *
 */
public class BaseServiceUtils {
	
	/**
	 * 根据UserId获取用户信息
	 * @param userMapper  {@link UserMapper}
	 * @param redisUtils  {@link RedisUtils}
	 * @param domainKey   User的key
	 * @param userId      用户Id
	 * @return            User对象
	 * @throws Exception   异常信息
	 */
	public static User getUserByUserId(UserMapper userMapper,RedisUtils redisUtils,String domainKey,Integer userId) throws Exception{
		User user=(User) redisUtils.getHashObject(domainKey, userId+"");   //根据Id获取缓存中的user对象
		//如果从缓存中查询到信息，直接返回即可
		if (user!=null) {
			return user;   
		}
		
		user=userMapper.selectByPrimaryKey(userId);  //获取数据库中的信息
		
		//如果从mysql中查询到信息，写入缓存
		if (user!=null) {
			redisUtils.addHashObject(domainKey, userId+"", user);  //写入缓存
		}
		return user;   //直接返回即可，不用判断是否为null
	}
	
	
	/**
	 * 添加动态
	 * @param dynamicMapper  
	 * @param redisUtils
	 * @param domainKey
	 * @param dynamic  动态
	 * @return
	 * @throws Exception
	 */
	public static void addDynamic(ProjectDynamicMapper dynamicMapper,RedisUtils redisUtils,String domainKey,ProjectDynamic dynamic,Long timeout) throws Exception{
		//先添加到mysql中
		int count=dynamicMapper.insertSelective(dynamic);
		
		//如果没有添加成功，直接抛出异常，回滚数据
		if (count==0) {
			throw new RuntimeException();
		}
		
		//如果添加成功，添加到缓存中
		try {
			//从右边添加
			redisUtils.addLeftListObject(domainKey+dynamic.getProjectId(), dynamic);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

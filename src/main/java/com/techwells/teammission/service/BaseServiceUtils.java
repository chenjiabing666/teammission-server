package com.techwells.teammission.service;

import com.techwells.teammission.dao.UserMapper;
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
}

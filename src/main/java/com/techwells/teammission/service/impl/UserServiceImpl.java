package com.techwells.teammission.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.PagingTool;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultErrorInfo;
import com.techwells.teammission.util.ResultSuccessInfo;
import com.techwells.teammission.util.StringUtil;

@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private RedisUtils redisUtils;
	
	@Value("#{redisParameter.userHashKey}")
	private String USER_KEY;   //User中hash的key
	
	@Value("#{redisParameter.userExpireTime}")
	private Long EXPIRE_TIME;   //过期时间
	
	@Override
	public Object addUser(User user) throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		/**
		 * 两步 ：
		 * 	1. 添加书到mysql中
		 * 	2、添加数据到redis中，使用hash-key-field的方式存储
		 */
		
		//添加到mysql数据中
		userMapper.insertSelective(user);
		try {
			//添加数据到redis中
			redisUtils.addHashObject(USER_KEY,user.getId()+"",user);
			rsInfo.setSuccessCode("100200");
			rsInfo.setSuccessMessage("添加成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，数据库回滚
		}
	}

	@Override
	public Object getUserByUserId(Integer userId)throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		
		//先从缓存中获取数据
		User user=null;
		try {
			user=(User) redisUtils.getHashObject(USER_KEY, userId+"");  //从缓存中获取数据
		} catch (Exception e) {
			throw new RuntimeException();  
		}
		
		//从缓存中查询到数据了,那么直接返回即可
		if (user!=null) {
			rsInfo.setResult(user);
			rsInfo.setSuccessCode("100200");
			rsInfo.setTotal(1);
			rsInfo.setSuccessMessage("获取数据成功");
			return rsInfo;
		}
		
		//从mysql中获取数据
		user=userMapper.selectByPrimaryKey(userId);
		
		if (user==null) {
			reInfo.setErrorCode("100002");
			reInfo.setErrorMessage("该用户不存在");
			return reInfo;
		}
		
		//将从mysql中获取的数据写入缓存
		try {
			redisUtils.addHashObject(USER_KEY, userId+"", user);
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚
		}
		rsInfo.setResult(user);
		rsInfo.setSuccessCode("100200");
		rsInfo.setTotal(1);
		rsInfo.setSuccessMessage("获取数据成功");
		return rsInfo;
	}

	@Override
	public Object modifyUser(User user)throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		
		//修改mysql中的数据
		int count=userMapper.updateByPrimaryKeySelective(user);  //更新数据库中的内容
		
		if (count==0) {
			reInfo.setErrorCode("100001");
			reInfo.setErrorMessage("修改用户失败");
			return reInfo;
		}
		
		try {
			//修改缓存中的数据，在添加之前一定需要根据用户id获取用户完整信息，否则将会丢失未更新的数据
			redisUtils.modifyHashObject(USER_KEY, user.getId()+"", user);
			rsInfo.setSuccessMessage("修改用户信息成功");
			rsInfo.setTotal(1);
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚数据
		}
		
	}

	@Override
	public Object deleteUserByUserId(Integer userId)throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		//删除数据库中的数据
		int count=userMapper.deleteByPrimaryKey(userId);
		if (count==0) {
			reInfo.setErrorCode("100001");
			reInfo.setErrorMessage("删除用户失败");
			return reInfo;
		}
		
		//删除缓存中的数据
		try {
			redisUtils.deleteHashObject(USER_KEY, userId+"");
			rsInfo.setSuccessMessage("删除成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚数据
		}
	}

	@Override
	public Object deleteUserBatch(String[] userIds)throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		
		//将数据库中的数据删除
		int count=userMapper.deleteBatchByUserIds(userIds);
		if (count==0) {
			reInfo.setErrorCode("100001");
			reInfo.setErrorMessage("删除失败");
			return reInfo;
		}
		
		//从缓存中删除数据
		try {
			redisUtils.deleteHashObjectBatch(USER_KEY,userIds);
			rsInfo.setSuccessMessage("删除成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚数据
		}
	}

	@Override
	public Object getUserList(PagingTool pagingTool)throws Exception {
		ResultSuccessInfo rsInfo=new ResultSuccessInfo();
		ResultErrorInfo reInfo=new ResultErrorInfo();
		//从缓存中获取数据
		List<User> users=null;
		String key=RedisUtils.getRedisKey(USER_KEY,"getUserList",pagingTool);
		try {
			//倒序获取，保证mysql和redis取出的数据顺序一致
			List<Object> objects=redisUtils.getRangeListObject(key,0,-1);
			if (objects.size()>0) {
				System.out.println("从缓存中查询数据");
				rsInfo.setResult(objects);
				rsInfo.setSuccessMessage("获取用户信息成功");
				return rsInfo;
			}
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚数据
		}
		//从数据库中获取数据
		users=userMapper.selectUserList(pagingTool);
		
		//写入缓存
		try {	
			//从表尾添加数据
			redisUtils.addRigthListObjectBatch(key, RedisUtils.convertToCollection(users), EXPIRE_TIME, TimeUnit.SECONDS);
			rsInfo.setSuccessMessage("获取用户信息成功");
			rsInfo.setResult(users);
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException();  //抛出异常，回滚数据
		}
		
	}

}

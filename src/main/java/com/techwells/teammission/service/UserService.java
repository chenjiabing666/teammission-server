package com.techwells.teammission.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.User;
import com.techwells.teammission.util.PagingTool;

@Transactional
public interface UserService {
	/**
	 * 添加User
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Object addUser(User user)throws Exception;
	/**
	 * 根据id获取User
	 * @param userId
	 * @return
	 */
	Object getUserByUserId(Integer userId)throws Exception;
	
	/**
	 * 修改User 
	 * @param user
	 * @return
	 */
	Object modifyUser(User user)throws Exception;
	
	/**
	 * 根据id删除User
	 * @param userId
	 * @return
	 */
	Object deleteUserByUserId(Integer userId)throws Exception;
	
	/**
	 * 根据id批量删除User
	 * @param userIds
	 * @return
	 */
	Object deleteUserBatch(String[] userIds)throws Exception;
	
	/**
	 * 根据条件分页获取User列表 
	 * @param pagingTool  {@link PagingTool}  
	 * @return  列表
	 */
	Object getUserList(PagingTool pagingTool)throws Exception;
}

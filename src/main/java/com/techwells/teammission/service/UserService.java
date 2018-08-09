package com.techwells.teammission.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

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
	 * 修改User ，修改个人的基本信息
	 * @param user
	 * @return
	 */
	Object modifyUser(User user)throws Exception;
	
	/**
	 * 修改用户账号和密码
	 * @param userId  用户Id
	 * @param email   邮箱
	 * @param userName  用户账号  手机号码
	 * @param oldPwd    旧密码
	 * @param newPwd    新密码
	 * @return
	 */
	Object modifyPassword(Integer userId,String oldPwd,String newPwd)throws Exception;
	
	
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
	 * 绑定手机号码
	 * @param userId 用户id
	 * @param mobile  手机号码
	 * @return
	 */
	Object bindMobile(Integer userId,String mobile)throws Exception;
	
	
	/**
	 * 取消绑定手机号码或者邮箱  
	 * @param userId  用户Id
	 * @param type 表示类型，mobile或者email
	 * @return
	 */
	Object unbindMobileOrEmail(Integer userId,String type)throws Exception;
	
	/**
	 * 绑定邮箱
	 * @param userId  用户Id
	 * @param email 邮箱
	 */
	Object bindEmail(Integer userId,String email);
	
	
	
	
	
	
	
	
	
}

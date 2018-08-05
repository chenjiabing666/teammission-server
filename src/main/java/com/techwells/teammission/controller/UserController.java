package com.techwells.teammission.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sun.management.resources.agent;

import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.DateUtil;
import com.techwells.teammission.util.PagingTool;
import com.techwells.teammission.util.ResultErrorInfo;
import com.techwells.teammission.util.ResultSuccessInfo;

@RestController
public class UserController {
	
	private static Logger logger=Logger.getLogger(UserController.class);  //log4j
	
	@Resource
	private UserService userService;
	
	/**
	 * 根据id获取用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/getUserByUserId")
	public Object getUserByUserId(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		
		//判断参数
		String userId=request.getParameter("userId");
		if (StringUtils.isEmpty(userId)) {
			reinfo.setErrorCode("100001");
			reinfo.setErrorMessage("用户id不能为空");
			return reinfo;
		}
		try {
			Object object=userService.getUserByUserId(Integer.parseInt(userId));
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100002");
			reinfo.setErrorMessage("获取用户数据异常");
			return reinfo;
		}
	}
	
	/**
	 * 添加用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/addUser")
	public Object addUser(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		
		String name=request.getParameter("userName");
		String age=request.getParameter("age");
		
		if (StringUtils.isEmpty(name)) {
			reinfo.setErrorCode("100011");
			reinfo.setErrorMessage("用户姓名不能为空");
			return reinfo;
		}
		
		if (StringUtils.isEmpty(age)) {
			reinfo.setErrorCode("100012");
			reinfo.setErrorMessage("用户年龄不能为空");
			return reinfo;
		}
		
		User user=new User();
		user.setName(name);
		user.setAge(Integer.parseInt(age));
		user.setCreateddate(new Date());
		
		try {
			Object object=userService.addUser(user);
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100013");
			reinfo.setErrorMessage("添加用户异常");
			return reinfo;
		}
	}
	
	
	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/modifyUser")
	public Object modifyUser(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		String userId=request.getParameter("userId");
		String name=request.getParameter("name");
		String birthday=request.getParameter("birthday");
		if (StringUtils.isEmpty(userId)) {
			reinfo.setErrorCode("100011");
			reinfo.setErrorMessage("用户id不能为空");
			return reinfo;
		}
		
		if (StringUtils.isEmpty(name)) {
			reinfo.setErrorCode("100012");
			reinfo.setErrorMessage("用户姓名不能为空");
			return reinfo;
		}
		
		if (StringUtils.isEmpty(birthday)) {
			reinfo.setErrorCode("100013");
			reinfo.setErrorMessage("用户生日不能为空");
			return reinfo;
		}
		
		Date bir=null;
		
		try {
			//将字符串转换成日期
			bir=DateUtil.getDateFromString(birthday,"yyyy-MM-dd");
		} catch (ParseException e1) {
			reinfo.setErrorCode("100016");
			reinfo.setErrorMessage("日期格式错误");
			return reinfo;
		}
		
		User user=null;
		try {
			Object object=userService.getUserByUserId(Integer.parseInt(userId));
			if(object instanceof ResultSuccessInfo){
				user=(User) ((ResultSuccessInfo)object).getResult();
			}else {   //表示出现了异常信息，因此直接返回给客户端即可
				return object;
			}
		} catch (Exception e) {
			reinfo.setErrorCode("100014");
			reinfo.setErrorMessage("获取用户信息异常");
			return reinfo;
		}
		
		if (user==null) {
			reinfo.setErrorCode("100015");
			reinfo.setErrorMessage("该用户不存在");
			return reinfo;
		}

		user.setUpdatedate(new Date());
		user.setName(name);
		user.setBirthday(bir);
		try {
			Object object=userService.modifyUser(user);
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100016");
			reinfo.setErrorMessage("修改用户异常");
			return reinfo;
		}
	}
	
	/**
	 * 根据id删除用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/deleteUser")
	public Object deleteByUser(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		String userId=request.getParameter("userId");
		
		if (StringUtils.isEmpty(userId)) {
			reinfo.setErrorCode("100011");
			reinfo.setErrorMessage("用户Id不能为空");
			return reinfo;
		}
		
		try {
			Object object=userService.deleteUserByUserId(Integer.parseInt(userId));
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100012");
			reinfo.setErrorMessage("删除用户信息异常");
			return reinfo;
		}
	}
	
	/**
	 * 批量删除用户信息  根据用户id
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/deleteUserBatch")
	public Object deleteUserBatch(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		String[] userIds=request.getParameterValues("userIds");
		
		if (userIds==null||userIds.length==0) {
			reinfo.setErrorCode("100010");
			reinfo.setErrorMessage("用户id不能为空");
			return reinfo;
		}
		
		try {
			Object object=userService.deleteUserBatch(userIds);
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100011");
			reinfo.setErrorMessage("删除用户异常");
			return reinfo;
		}
		
	}
	
	/**
	 * 分页获取用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/getUserList")
	public Object getUserList(HttpServletRequest request){
		ResultErrorInfo reinfo=new ResultErrorInfo();
		String pageNum=request.getParameter("pageNum");
		String pageSize=request.getParameter("pageSize");
		String age=request.getParameter("age");
		
		if (StringUtils.isEmpty(pageNum)) {
			reinfo.setErrorCode("100010");
			reinfo.setErrorMessage("当前页数不能为空");
			return reinfo;
		}
		
		
		if (StringUtils.isEmpty(pageSize)) {
			reinfo.setErrorCode("100011");
			reinfo.setErrorMessage("每页显示的数量不能为空");
			return reinfo;
		}
		
		if (StringUtils.isEmpty(age)) {
			reinfo.setErrorCode("100012");
			reinfo.setErrorMessage("用户年龄不能为空");
			return reinfo;
		}
		
		PagingTool pagingTool=new PagingTool(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		Map<String, Object> params=new HashedMap<String, Object>();

		pagingTool.setParams(params);
		
		params.put("age",Integer.parseInt(age));
		 try {
			Object object=userService.getUserList(pagingTool);
			return object;
		} catch (Exception e) {
			reinfo.setErrorCode("100013");
			reinfo.setErrorMessage("获取用户信息异常");
			return reinfo;
		}
	}
	
}

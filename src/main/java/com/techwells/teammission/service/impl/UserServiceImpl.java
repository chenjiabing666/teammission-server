package com.techwells.teammission.service.impl;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.techwells.teammission.dao.ProjectMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.dao.UserProjectMapper;
import com.techwells.teammission.domain.Project;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.domain.UserProject;
import com.techwells.teammission.service.BaseServiceUtils;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.PagingTool;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper userMapper;

	@Resource
	private RedisUtils redisUtils;
	
	@Resource
	private ProjectMapper projectMapper;
	
	@Resource
	private UserProjectMapper userProjectMapper;
	
	@Value("#{redisParameter.userHashKey}")
	private String USER_KEY; // User中hash的key

	@Value("#{redisParameter.userExpireTime}")
	private Long USER_EXPIRE_TIME; // 过期时间
	
	@Value("#{redisParameter.projectHashKey}")
	private String PROJECT_KEY;     //项目在缓存中对应的key
	
	@Value("#{redisParameter.projectExpireTime}")
	private Long PROJECT_EXPIRE_TIME;      //项目缓存的过期时间
	


	@Override
	public Object addUser(User user) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集
		/**
		 * 两步 ： 1. 添加书到mysql中 2、添加数据到redis中，使用hash-key-field的方式存储
		 */

		// 添加到mysql数据中
		userMapper.insertSelective(user);
		try {
			// 添加数据到redis中
			redisUtils.addHashObject(USER_KEY, user.getUserId() + "", user);
			rsInfo.setMessage("添加成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，数据库回滚
		}
	}

	@Override
	public Object getUserByUserId(Integer userId) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集

		// 先从缓存中获取数据
		User user = null;
		try {
			user = (User) redisUtils.getHashObject(USER_KEY, userId + ""); // 从缓存中获取数据
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// 从缓存中查询到数据了,那么直接返回即可
		if (user != null) {
			rsInfo.setResult(user);
			rsInfo.setTotal(1);
			rsInfo.setMessage("获取数据成功");
			return rsInfo;
		}

		// 从mysql中获取数据
		user = userMapper.selectByPrimaryKey(userId);

		if (user == null) {
			rsInfo.setCode("100002");
			rsInfo.setMessage("该用户不存在");
			return rsInfo;
		}

		// 将从mysql中获取的数据写入缓存
		try {
			redisUtils.addHashObject(USER_KEY, userId + "", user);
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚
		}
		rsInfo.setResult(user);
		rsInfo.setTotal(1);
		rsInfo.setMessage("获取数据成功");
		return rsInfo;
	}

	@Override
	public Object modifyUser(User user) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集

		// 修改mysql中的数据
		int count = userMapper.updateByPrimaryKeySelective(user); // 更新数据库中的内容

		if (count == 0) {
			rsInfo.setCode("100001");
			rsInfo.setMessage("修改用户失败");
			return rsInfo;
		}
		
		
		User user2=userMapper.selectByPrimaryKey(user.getUserId());   //根据Id获取修改后的信息
		
		try {
			
			// 修改缓存中的数据，在添加之前一定需要根据用户id获取用户完整信息，否则将会丢失未更新的数据
			redisUtils.modifyHashObject(USER_KEY, user.getUserId() + "", user2);
			rsInfo.setMessage("修改用户信息成功");
			rsInfo.setTotal(1);
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚数据
		}

	}

	@Override
	public Object deleteUserByUserId(Integer userId) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集
		// 删除数据库中的数据
		int count = userMapper.deleteByPrimaryKey(userId);
		if (count == 0) {
			rsInfo.setCode("100001");
			rsInfo.setMessage("删除用户失败");
			return rsInfo;
		}

		// 删除缓存中的数据
		try {
			redisUtils.deleteHashObject(USER_KEY, userId + "");
			rsInfo.setMessage("删除成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚数据
		}
	}

	@Override
	public Object deleteUserBatch(String[] userIds) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集
		// 将数据库中的数据删除
		int count = userMapper.deleteBatchByUserIds(userIds);
		if (count == 0) {
			rsInfo.setCode("100001");
			rsInfo.setMessage("删除失败");
			return rsInfo;
		}

		// 从缓存中删除数据
		try {
			redisUtils.deleteHashObjectBatch(USER_KEY, userIds);
			rsInfo.setMessage("删除成功");
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚数据
		}
	}

	@Override
	public Object getUserList(PagingTool pagingTool) throws Exception {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集
		// 从缓存中获取数据
		List<User> users = null;
		String key = RedisUtils
				.getRedisKey(USER_KEY, "getUserList", pagingTool);
		try {
			// 倒序获取，保证mysql和redis取出的数据顺序一致
			List<Object> objects = redisUtils.getRangeListObject(key, 0, -1);
			if (objects.size() > 0) {
				System.out.println("从缓存中查询数据");
				rsInfo.setResult(objects);
				rsInfo.setMessage("获取用户信息成功");
				return rsInfo;
			}
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚数据
		}
		// 从数据库中获取数据
		users = userMapper.selectUserList(pagingTool);

		// 写入缓存
		try {
			// 从表尾添加数据
			redisUtils.addRigthListObjectBatch(key,
					RedisUtils.convertToCollection(users), USER_EXPIRE_TIME,
					TimeUnit.SECONDS);
			rsInfo.setMessage("获取用户信息成功");
			rsInfo.setResult(users);
			return rsInfo;
		} catch (Exception e) {
			throw new RuntimeException(); // 抛出异常，回滚数据
		}

	}

	@Override
	public Object modifyPassword(Integer userId,
			String oldPwd, String newPwd) {
		ResultInfo rsInfo=new ResultInfo();  //封装结果集
			
		//根据Id获取用户的详细信息
		//先从缓存中获取
		User user=null;
		try {
			user=(User) redisUtils.getHashObject(USER_KEY, userId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//缓存中没有这个数据，从mysql中获取详细的信息
		if (user==null) {
			user=userMapper.selectByPrimaryKey(userId);  //从mysql中获取数据
			//从mysql中也没有获取到这个数据
			if (user==null) {
				rsInfo.setCode("100001");
				rsInfo.setMessage("该用户不存在，可能已经注销了");
				return rsInfo;
			}
		}
		
		//user!=null
		
		//如果密码不相同
		if (!user.getPassword().equals(oldPwd)) {
			rsInfo.setCode("100002");
			rsInfo.setMessage("密码不正确，请重新输入");
			return rsInfo;
		}
		
		//密码正确，可以修改
		user.setPassword(newPwd);
		
		//修改mysql中的数据
		int count=userMapper.updateByPrimaryKeySelective(user);
		//修改失败
		if (count==0) {
			rsInfo.setCode("100003");
			rsInfo.setMessage("修改密码失败");
			return rsInfo;
		}
		
		//修改成功之后，修改缓存中的信息
		try {
			redisUtils.addHashObject(USER_KEY, user.getUserId()+"", user);  //修改redis中的信息
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		rsInfo.setMessage("修改密码成功");
		return rsInfo;
	}

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

	
	/**
	 * 绑定手机号码
	 * 1、根据mobile在数据库中查询该手机号码是否已经绑定过了
	 * 2、修改mysql中的用户信息
	 * 3、修改缓存中的信息
	 */
	@Override
	public Object bindMobile(Integer userId, String mobile)throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		//1、根据mobile在数据库中查询该手机号码是否已经绑定过了
		User user=userMapper.selectUserByMobile(mobile);
		
		if (user!=null) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("该手机号码已经被别人绑定了");
			return resultInfo;
		}
		
		//user==null，表示没有人绑定，那么现在可以绑定了，其实就是修改用户的信息
		
		//先从缓存中获取信息
		try {
			redisUtils.getHashObject(USER_KEY, userId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//如果user==null,表示缓存中没有数据，此时查询数据库
		if (user==null) {
			user=userMapper.selectByPrimaryKey(userId);  //从mysql中查询
			//mysql中也不存在这个数据
			if (user==null) {
				resultInfo.setCode("100002");
				resultInfo.setMessage("该用户不存在，可能已经被删除了");
				return resultInfo;
			}
		}
		
		//user!=null，修改手机号码即可
		user.setMobile(mobile);
		
		//修改mysql
		int count=userMapper.updateByPrimaryKeySelective(user);
		
		if (count==0) {
			resultInfo.setCode("100003");
			resultInfo.setMessage("绑定失败");
			return resultInfo;
		}
		
		//修改缓存中的信息
		try {
			redisUtils.addHashObject(USER_KEY, user.getUserId()+"", user);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("绑定手机号码成功");
		return resultInfo;
	}

	@Override
	public Object unbindMobileOrEmail(Integer userId, String type)
			throws Exception {
		ResultInfo resultInfo=new ResultInfo();
		//根据用户id获取用户信息
		
		User user=null;
		try {
			user=(User) redisUtils.getHashObject(USER_KEY, userId+"");   //从缓存中获取数据
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		
		if (userId==null) {
			user=userMapper.selectByPrimaryKey(userId);  //从mysql中获取数据
			if (user==null) {
				resultInfo.setCode("100001");
				resultInfo.setMessage("该用户不存在，账号可能已经被注销了");
				return resultInfo;
			}
		}
		
		//user!=null
		
		//取消绑定手机
		if (type.equals("mobile")) {
			user.setMobile(null);
		}else if (type.equals("email")) {
			user.setEmail(null);
		}else {
			resultInfo.setCode("100002");
			resultInfo.setMessage("请输入正确的取绑类型");
			return resultInfo;
		}
		
		
		//修改数据中的数据
		int count=userMapper.updateByPrimaryKeySelective(user);
		
		if (count==0) {
			resultInfo.setCode("100003");
			resultInfo.setMessage("取绑失败");
			return resultInfo;
		}
		
		//修改缓存中的数据
		try {
			redisUtils.addHashObject(USER_KEY, user.getUserId()+"", user);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("取绑成功");
		return resultInfo;
	}

	@Override
	public Object bindEmail(Integer userId, String email) {
		ResultInfo resultInfo=new ResultInfo();
		//根据eamil查询数据库，看看是否已经绑定了
		User user=userMapper.selectUserByEmail(email);
		if (user!=null) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("该邮箱已被绑定");
			return resultInfo;
		}
		
		//user==null，改邮箱没有被绑定
		try {
			user=(User) redisUtils.getHashObject(USER_KEY, userId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		//缓存中没有数据
		if (user==null) {
			user=userMapper.selectByPrimaryKey(userId);
			if(user==null){
				resultInfo.setCode("100002");
				resultInfo.setMessage("该用户不存在");
				return resultInfo;
			}
		}
		
		user.setEmail(email);
		int count=userMapper.updateByPrimaryKeySelective(user);
		if (count==0) {
			resultInfo.setCode("100003");
			resultInfo.setMessage("绑定失败");
			return resultInfo;
		}
		
		//修改缓存中的信息
		try {
			redisUtils.addHashObject(USER_KEY, user.getUserId()+"", user);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("绑定成功");
		
		return resultInfo;
	}

}

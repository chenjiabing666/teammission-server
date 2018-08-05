package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Person;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.PersonService;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultSuccessInfo;
import com.techwells.teammission.util.StringUtil;

public class UserTest {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		UserService userService=context.getBean("userServiceImpl",UserService.class);
		User user=new User();
		user.setId(2);
		user.setAge(22);
		user.setBirthday(new Date());
		user.setCreateddate(new Date());
		user.setName("jack");
		try {
			userService.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常回滚数据");
		}
		context.close();
	}
	
	
	@Test
	public void test7(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		UserService userService=context.getBean("userServiceImpl",UserService.class);
		try {
			Object object=userService.getUserByUserId(7);
			if (object instanceof ResultSuccessInfo) {
				ResultSuccessInfo rsinfo=(ResultSuccessInfo)object;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常回滚");
		}
		context.close();
	}
	
	//user_createdDate=Sun Aug 05 15:12:16 CST 2018_age=22_userId=1_
	@Test
	public void test8(){
		Map<String, Object> params=new HashedMap<String, Object>();
		params.put("userId", 1);
		params.put("createdDate","2012-11-09");
		params.put("age", 22);
		System.out.println(StringUtil.getRedisKey("user", params));
		System.out.println(StringUtil.getRedisKey("user", params).equals("user_createdDate=2012-11-09_age=22_userId=1_"));
	}
	
	
	@Test
	public void test9(){
		List<User> users=new ArrayList<User>();
		User user=new User();
		user.setAge(22);
		user.setBirthday(new Date());
		users.add(user);
//		Set<Object> set=(Set<Object>) RedisUtils.convertToCollection(users);
	}
	
	
	
}

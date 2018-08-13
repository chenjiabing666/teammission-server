package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sun.launcher.resources.launcher;

import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Interface;
import com.techwells.teammission.domain.ProjectImage;
import com.techwells.teammission.domain.User;
import com.techwells.teammission.service.UserService;
import com.techwells.teammission.util.RedisUtils;

public class TestRedis {
	
	@Test
	public void test1() throws Exception{
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		List<ProjectImage> projectImages=new ArrayList<ProjectImage>();
		RedisUtils redisUtils=context.getBean("redisUtils",RedisUtils.class);
		List<Interface> faces=(List<Interface>) redisUtils.getHashObject("ImageInterFace", "1");
		System.out.println(faces);
	}
	
	@Test
	public void test2(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
//		UserService userService=context.getBean("userServiceImpl",UserService.class);
//		UserMapper userMapper=context.getBean("userMapper",UserMapper.class);
		RedisUtils redisUtils=context.getBean("redisUtils",RedisUtils.class);
		
		try {
			redisUtils.deleteKey("project");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}


class Person implements Serializable{
	private static final long serialVersionUID = -8790097899496712929L;
	private String name;
	private int age;
	private List<User> users;
//	private User user;
	
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", users=" + users
				+ "]";
	}
	
	
	
}

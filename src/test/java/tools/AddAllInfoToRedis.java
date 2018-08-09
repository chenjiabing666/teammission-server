package tools;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 添加所有的信息到缓存中
 * 作用：开发的时候需要用到，如果这个键存在，那么就直接更新
 * @author Administrator
 *
 */
public class AddAllInfoToRedis {
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		                                      
	}
}	

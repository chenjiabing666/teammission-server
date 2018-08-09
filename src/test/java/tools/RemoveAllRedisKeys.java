package tools;

import static org.hamcrest.CoreMatchers.containsString;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techwells.teammission.util.RedisUtils;

/**
 * 删除Redis中的所有key，便于测试
 * @author Administrator
 *
 */
public class RemoveAllRedisKeys {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		RedisUtils redisUtils=context.getBean("redisUtils",RedisUtils.class);
		redisUtils.delteAllKeys();
		context.close();
	}
}

package test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techwells.teammission.dao.NoticeMapper;
import com.techwells.teammission.dao.UserMapper;
import com.techwells.teammission.domain.Notice;
import com.techwells.teammission.domain.User;

public class UserTest {
	
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		UserMapper userMapper=context.getBean("userMapper",UserMapper.class);
		NoticeMapper noticeMapper=context.getBean("noticeMapper",NoticeMapper.class);
		Notice notice=noticeMapper.selectByPrimaryKey(1);
		System.out.println(notice.getStatus());
		User user=userMapper.selectByPrimaryKey(1);
		System.out.println(user);
		context.close();
		
	}
}

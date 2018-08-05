package test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techwells.teammission.domain.Person;
import com.techwells.teammission.service.PersonService;

public class PeronTest {
	@Test
	public void test1(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		PersonService personService=context.getBean("personServiceImpl",PersonService.class);
		Person person=new Person();
		person.setAge(22);
		person.setBirthday(new Date());
		person.setEmail("cd");
		person.setName("cdcsd");
		try {
			personService.addPerson(person);
		} catch (Exception e) {
			System.out.println("异常回滚");
			e.printStackTrace();
		}
		context.close();
	}
	
	
	@Test
	public void test2(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		PersonService personService=context.getBean("personServiceImpl",PersonService.class);
		Person person=new Person();
		person.setAge(22);
		try {
			personService.getPerson();
		} catch (Exception e) {
			System.out.println("异常回滚");
			e.printStackTrace();
		}
		context.close();
	}
	
	
	
	@Test
	public void test3(){
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
		PersonService personService=context.getBean("personServiceImpl",PersonService.class);
		try {
			Set<String> set=new HashSet<String>();
			set.add("p1");
			set.add("a");
			personService.deetePersonBatch(set);
		} catch (Exception e) {
			System.out.println("异常回滚");
			e.printStackTrace();
		}
		context.close();
	}
}

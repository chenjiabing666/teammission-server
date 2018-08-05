package com.techwells.teammission.service.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.techwells.teammission.domain.Person;
import com.techwells.teammission.service.PersonService;
import com.techwells.teammission.util.RedisUtils;

@Service
public class PersonServiceImpl implements PersonService{
	@Resource
	private RedisUtils redisUtils;
	
	@Override
	public Object addPerson(Person person) throws Exception {
		redisUtils.addStringObject("p12", person);
		System.out.println(10/0);
		return null;
	}
	
	@Override
	public Object getPerson() throws Exception {
		Person person=(Person) redisUtils.getStringObject("p12");
		System.out.println(person);
		return null;
	}

	@Override
	public Object deletePerson(String key) throws Exception {
		redisUtils.deleteObject(key);
		System.out.println(10/0);
		return null;
	}

	
	@Override
	public Object deetePersonBatch(Set<String> keys) {
//		redisUtils.deleteObjectBatch(keys);
//		System.out.println(10/0);
		return null;
	}
	
	
	
	
	
}

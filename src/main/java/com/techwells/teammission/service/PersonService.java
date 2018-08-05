package com.techwells.teammission.service;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.techwells.teammission.domain.Person;

@Transactional
public interface PersonService {
	Object addPerson(Person person)throws Exception;
	Object getPerson()throws Exception;
	Object deletePerson(String key)throws Exception;
	
	Object deetePersonBatch(Set<String> keys);
}

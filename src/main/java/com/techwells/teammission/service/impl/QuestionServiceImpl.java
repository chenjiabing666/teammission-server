package com.techwells.teammission.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.sun.org.apache.regexp.internal.recompile;
import com.techwells.teammission.dao.QuestionMapper;
import com.techwells.teammission.domain.Question;
import com.techwells.teammission.service.QuestionService;
import com.techwells.teammission.util.RedisUtils;
import com.techwells.teammission.util.ResultInfo;

@Service
public class QuestionServiceImpl implements QuestionService {
	
	@Resource
	private QuestionMapper questionMapper;
	
	@Resource
	private RedisUtils redisutils;
	
	@Value("#{redisParameter.questionHashKey}")
	private String 	QUESTION_HASH_KEY;     //存储问题的hash key

	@Override
	public Object addQuestion(Question question) {
		ResultInfo resultInfo=new ResultInfo();
		//添加到数据库中
		int count=questionMapper.insertSelective(question);
		
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("添加失败");
			return resultInfo;
		}
		
		//添加成功
		
		//添加到缓存中
		
		try {
			redisutils.addHashObject(QUESTION_HASH_KEY, question.getQuestionId()+"", question);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		resultInfo.setMessage("添加成功");
		return resultInfo;
	}

	@Override
	public Object deleteQuestion(Integer questionId) {
		//删除数据库中的信息
		ResultInfo resultInfo=new ResultInfo();
		int count=questionMapper.deleteByPrimaryKey(questionId);
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("删除失败");
			return resultInfo;
		}
		
		//删除缓存中的数据
		List<Object> questions=null;
		try {
			redisutils.deleteHashObject(QUESTION_HASH_KEY, questionId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		resultInfo.setMessage("删除成功");
		return resultInfo;
	}

	@Override
	public Object getQuestionById(Integer qustionId) {
		ResultInfo resultInfo=new ResultInfo();
		//从缓存之中获取数据
		Question question=null;
		try {
			question=(Question) redisutils.getHashObject(QUESTION_HASH_KEY, qustionId+"");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		if (question!=null) {
			resultInfo.setResult(question);
			resultInfo.setTotal(1);
			resultInfo.setMessage("成功获取数据");
		}
		
		//从mysql中获取数据
		question=questionMapper.selectByPrimaryKey(qustionId);  
		
		if (question==null) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("该问题已被删除");
			return resultInfo;
		}
		
		//添加进入缓存
		
		try {
			redisutils.addHashObject(QUESTION_HASH_KEY, qustionId+"", question);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		resultInfo.setResult(question);
		resultInfo.setTotal(1);
		resultInfo.setMessage("成功获取数据");
		return resultInfo;
	}

	
	@Override
	public Object modifyQuestion(Question question) {
		ResultInfo resultInfo=new ResultInfo();
		//修改数据库中的信息
		int count=questionMapper.updateByPrimaryKeySelective(question);
		//修改失败
		if (count==0) {
			resultInfo.setCode("100001");
			resultInfo.setMessage("修改失败");
			return resultInfo;
		}
		//修改成功之后，修改缓存中的数据
		
		//根据Id从数据库中获取完整的信息
		Question question2=questionMapper.selectByPrimaryKey(question.getQuestionId());
		
		if (question2==null) {
			throw new RuntimeException();  //直接抛出异常，回滚数据
		}
		
		try {
			redisutils.addHashObject(QUESTION_HASH_KEY, question.getQuestionId()+"", question2);  //修改缓存中的数据
		} catch (Exception e) {
			throw new RuntimeException();
		}
		resultInfo.setMessage("修改成功");
		return resultInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

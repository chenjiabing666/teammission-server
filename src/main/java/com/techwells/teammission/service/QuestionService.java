package com.techwells.teammission.service;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.Question;

/**
 * 周报的问题业务层
 * @author Administrator
 *
 */
@Transactional
public interface QuestionService {
	/**
	 * 添加周报的问题
	 * @param question  Question对象
	 * @return   返回结果集
	 */
	Object addQuestion(Question question);
	
	/**
	 * 根据id删除问题
	 * @param questionId
	 * @return
	 */
	Object deleteQuestion(Integer questionId);
	
	
	/**
	 * 根据Id获取问题
	 * @param qustionId
	 * @return
	 */
	Object getQuestionById(Integer qustionId);
	
	/**
	 * 修改问题
	 * @param question
	 * @return
	 */
	Object modifyQuestion(Question question);
	
	
	
	
	
	
}

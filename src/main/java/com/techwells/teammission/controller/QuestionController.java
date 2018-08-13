package com.techwells.teammission.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwells.teammission.domain.Question;
import com.techwells.teammission.service.QuestionService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

/**
 * 问题的controller
 * @author Administrator
 *
 */
@RestController
public class QuestionController {
	@Resource
	private QuestionService questionService;
	
	
	/**
	 * 添加问题
	 * @param request
	 * @return
	 */
	@RequestMapping("/question/addQuestion")
	public Object addQuestion(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String description=request.getParameter("description");  //描述
		String repyTime=request.getParameter("repyTime");  //回复时间
		String visibility=request.getParameter("visibility");  //设置回答的成员可见性
		String showUsersId=request.getParameter("showUsersId");   //设置可见的成员
		String replyWho=request.getParameter("replyWho");  //谁可以回答  1所有成员都可以回答，2 设置指定的成员回答
		String replyUsersId=request.getParameter("replyUsersId");  //设置回答的成员id，用逗号分割
		String remind=request.getParameter("remind");  //是否提醒
		
		//校验参数
		
		if (StringUtils.isEmpty(description)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("问题描述不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(repyTime)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("提问日期不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(visibility)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("设置回答成员不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(remind)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("是否提醒成员回答周报问题不能为空");
			return resultInfo;
		}
		
		if(!StringUtil.isNumber(visibility)){
			resultInfo.setCode("100015");
			resultInfo.setMessage("可见性只能为数字");
			return resultInfo;
		}
		
		//指定了回答的成员
		if (Integer.parseInt(visibility)==2&&StringUtils.isEmpty(showUsersId)) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("请设置可以看见的成员");
			return resultInfo;
		}
		
		if (Integer.parseInt(replyWho)==2&&StringUtils.isEmpty(replyUsersId)) {
			resultInfo.setCode("100017");
			resultInfo.setMessage("请设置可以回复的成员");
			return resultInfo;
		}
		
		Question question=new Question();
		
		question.setCreatedDate(new Date());
		question.setDescription(description);
		question.setRemind(Integer.parseInt(remind));
		question.setReplyUsersId(replyUsersId);
		question.setReplyWho(Integer.parseInt(replyWho));
		question.setRepyTime(repyTime);
		question.setVisibility(Integer.parseInt(visibility));
		question.setShowUsersId(showUsersId);
		
		
		try {
			Object object=questionService.addQuestion(question);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100018");
			resultInfo.setMessage("添加异常");
			return resultInfo;
		}
		
	}
	

	/**
	 * 根据Id获取数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/question/getQuestionById")
	public Object getQuestion(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String questionId=request.getParameter("questionId");
		if (StringUtils.isEmpty(questionId)) {
			resultInfo.setCode("100010");
			resultInfo.setMessage("问题Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(questionId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("问题Id只能是数字");
			return resultInfo;
		}
		
		try {
			Object object=questionService.getQuestionById(Integer.parseInt(questionId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("获取问题信息异常");
			return resultInfo;
		}
	}
	
	
	/**
	 * 根据Id删除问题
	 * @param request
	 * @return
	 */
	@RequestMapping("/question/deleteQuestionById")
	public Object deleteQuestionById(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String questionId=request.getParameter("questionId");
		
		//校验参数
		if (StringUtils.isEmpty(questionId)) {
			resultInfo.setCode("100010");
			resultInfo.setMessage("问题Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(questionId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("问题Id只能是数字");
			return resultInfo;
		}
		
		try {
			Object object=questionService.deleteQuestion(Integer.parseInt(questionId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("删除数据异常");
			return resultInfo;
		}
		
	}
	
	
	/**
	 * 根据Id修改数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/question/modifyQuestion")
	public Object modifyQuestion(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String description=request.getParameter("description");  //描述
		String repyTime=request.getParameter("repyTime");  //回复时间
		String visibility=request.getParameter("visibility");  //设置回答的成员可见性
		String showUsersId=request.getParameter("showUsersId");   //设置可见的成员
		String replyWho=request.getParameter("replyWho");  //谁可以回答  1所有成员都可以回答，2 设置指定的成员回答
		String replyUsersId=request.getParameter("replyUsersId");  //设置回答的成员id，用逗号分割
		String remind=request.getParameter("remind");  //是否提醒
		String questionId=request.getParameter("questionId");
		
		//校验参数
		
		if (StringUtils.isEmpty(questionId)) {
			resultInfo.setCode("100019");
			resultInfo.setMessage("问题Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(questionId)) {
			resultInfo.setCode("100020");
			resultInfo.setMessage("问题Id只能是数字");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(description)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("问题描述不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(repyTime)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("提问日期不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(visibility)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("设置回答成员不能为空");
			return resultInfo;
		}
		
		
		if (StringUtils.isEmpty(remind)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("是否提醒成员回答周报问题不能为空");
			return resultInfo;
		}
		
		if(!StringUtil.isNumber(visibility)){
			resultInfo.setCode("100015");
			resultInfo.setMessage("可见性只能为数字");
			return resultInfo;
		}
		
		//指定了回答的成员
		if (Integer.parseInt(visibility)==2&&StringUtils.isEmpty(showUsersId)) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("请设置可以看见的成员");
			return resultInfo;
		}
		
		if (Integer.parseInt(replyWho)==2&&StringUtils.isEmpty(replyUsersId)) {
			resultInfo.setCode("100017");
			resultInfo.setMessage("请设置可以回复的成员");
			return resultInfo;
		}
		
		Question question=new Question();
		
		question.setCreatedDate(new Date());
		question.setDescription(description);
		question.setRemind(Integer.parseInt(remind));
		question.setReplyUsersId(replyUsersId);
		question.setReplyWho(Integer.parseInt(replyWho));
		question.setRepyTime(repyTime);
		question.setVisibility(Integer.parseInt(visibility));
		question.setShowUsersId(showUsersId);
		
		try {
			Object object=questionService.modifyQuestion(question);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100018");
			resultInfo.setMessage("修改数据异常");
			return resultInfo;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

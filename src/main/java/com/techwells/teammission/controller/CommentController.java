package com.techwells.teammission.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwells.teammission.domain.Comment;
import com.techwells.teammission.service.CommentService;
import com.techwells.teammission.util.ResultInfo;
import com.techwells.teammission.util.StringUtil;

@RestController
public class CommentController {
	@Resource
	private CommentService commentService;
	
	/**
	 * 获取周报下的所有评论
	 * @param weeekId 周报Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/comment/getCommentByWeekId")
	public Object getComment(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String weeekId=request.getParameter("weeekId");
		
		if (StringUtils.isEmpty(weeekId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("周报Id不能为空");
			return resultInfo;
		}
		
		
		if (!StringUtil.isNumber(weeekId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("周报Id只能是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=commentService.getComment(Integer.parseInt(weeekId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("获取评论异常");
			return resultInfo;
		}
	}
	
	/**
	 * 添加评论
	 * @param weekId : 周报id
	 * @param content : 内容
	 * @param parentId : 父节点id,如果没有为null
	 * @param toUserId : 被评论的人  如果没有直接为null
	 * @param userId : 评论的人Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/comment/addComment")
	public Object addComment(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String weekId=request.getParameter("weekId");
		String content=request.getParameter("content");
		String parentId=request.getParameter("parentId");
		String toUserId=request.getParameter("toUserId");
		String userId=request.getParameter("userId");  //发布评论人的Id
		
		//校验参数
		
		if (StringUtils.isEmpty(weekId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("周报Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(content)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("评论内容不能为空");
			return resultInfo;
		}
		
		if (!StringUtils.isEmpty(toUserId)&&!StringUtil.isNumber(toUserId)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("回复的用户Id必须是数字");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("发布评论人的Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(userId)) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("发布评论人的Id只能是数字");
			return resultInfo;
		}
		
		if (!StringUtils.isEmpty(parentId)&&!StringUtil.isNumber(parentId)) {
			resultInfo.setCode("100017");
			resultInfo.setMessage("父节点的Id只能是数字");
			return resultInfo;
		}
		
		Comment comment=new Comment();
		comment.setContent(content);
		
		if (!StringUtils.isEmpty(parentId)) {
			comment.setParentId(Integer.parseInt(parentId));
		}
		
		if (!StringUtils.isEmpty(toUserId)) {
			comment.setToUserId(Integer.parseInt(toUserId));
		}
		comment.setCreatedDate(new Date());
		comment.setUserId(Integer.parseInt(userId));
		comment.setWeeklyId(Integer.parseInt(weekId));
		try {
			Object object=commentService.addComment(comment);
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("评论异常");
			return resultInfo;
		}
		
	}
	
	
	
	/**
	 * 获取某一条评论的所有对话
	 * @param userId : 评论的人id
	 * @param toUserId： 被回复人的Id
	 * @param commentId : 评论Id
	 * @param request
	 * @return
	 */
	@RequestMapping("/comment/getSomeOneComment")
	public Object getSomeOneComment(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String userId=request.getParameter("userId");
		String toUserId=request.getParameter("toUserId");
		String commentId=request.getParameter("commentId");
		
		//校验参数
		if (StringUtils.isEmpty(userId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("用户Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(toUserId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("回复人的Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(commentId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("评论Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(userId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("用户Id只能是数字");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(toUserId)) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("回复的人Id只能是数字");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(commentId)) {
			resultInfo.setCode("100015");
			resultInfo.setMessage("评论Id只能是数字");
			return resultInfo;
		}
		
		
		//调用service层的方法
		try {
			Object object=commentService.getSomeOneComment(Integer.parseInt(userId), Integer.parseInt(toUserId), Integer.parseInt(commentId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100016");
			resultInfo.setMessage("获取评论异常");
			return resultInfo;
		}
		
	}
	
	
	@RequestMapping("/comment/deleteComment")
	public Object deleteComment(HttpServletRequest request){
		ResultInfo resultInfo=new ResultInfo();
		String commentId=request.getParameter("commentId");
		String weekId=request.getParameter("weekId");
		
		//校验参数
		if (StringUtils.isEmpty(commentId)) {
			resultInfo.setCode("100011");
			resultInfo.setMessage("评论Id不能为空");
			return resultInfo;
		}
		
		if (StringUtils.isEmpty(weekId)) {
			resultInfo.setCode("100012");
			resultInfo.setMessage("周报Id不能为空");
			return resultInfo;
		}
		
		if (!StringUtil.isNumber(commentId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("评论Id只能是数字");
			return resultInfo;
		}
		

		if (!StringUtil.isNumber(commentId)) {
			resultInfo.setCode("100013");
			resultInfo.setMessage("评论Id只能是数字");
			return resultInfo;
		}
		
		
		try {
			Object object=commentService.deleteComment(Integer.parseInt(commentId), Integer.parseInt(weekId));
			return object;
		} catch (Exception e) {
			resultInfo.setCode("100014");
			resultInfo.setMessage("删除评论异常");
			return resultInfo;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

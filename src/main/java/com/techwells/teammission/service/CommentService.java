package com.techwells.teammission.service;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.Comment;

/**
 * 评论的业务逻辑
 * @author Administrator
 *
 */
@Transactional
public interface CommentService {
	
	/**
	 * 根据周报Id获取所有下面的所有评论
	 * @param weekId
	 * @return
	 * @throws Exception 
	 */
	Object getComment(Integer weekId) throws Exception;  
	
	/**
	 * 添加评论
	 * @param weekId  周报Id
	 * @param parentId  父类Id，如果没有填0
	 * @param toUserId  被评论的人，如果没有填0
	 * @param content  评论的内容
	 * @param userId  评论人的id，用于推送通知
	 * @return
	 * @throws Exception 
	 */
	Object addComment(Comment comment) throws Exception;
	
	/**
	 * 获取某一条评论的上面的所有对话
	 * @param userId  评论人的Id
	 * @param toUserId  回复给对方的Id
	 * @param commentId  内容Id
	 * @return  返回结果集
	 * @throws Exception
	 */
	Object getSomeOneComment(Integer userId,Integer toUserId,Integer commentId)throws Exception;
	
	
	/**
	 * 根据评论Id删除评论
	 * @param commentId  删除评论
	 * @param weekId  周报Id，更新缓存需要用到
	 * @return 
	 * @throws Exception
	 */
	Object deleteComment(Integer commentId,Integer weekId)throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.techwells.teammission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.techwells.teammission.domain.Comment;
import com.techwells.teammission.domain.rs.CommentUserVos;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);
    
    /**
     * 根据周报的Id获取所有的评论
     * @param weekId
     * @return
     */
    List<CommentUserVos> selectComentByWeekId(Integer weekId);
    
    CommentUserVos selectCommentUserVosByUserIdAndParentId(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId,@Param("commentId")Integer commentId);
    
    /**
     * 根据父节点获取所有的评论
     * @param parentId  父节点id
     * @return
     */
    List<Comment> selectCommentByParentId(@Param("parentId")Integer parentId,@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);
    
    
}
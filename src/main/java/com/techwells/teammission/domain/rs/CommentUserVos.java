package com.techwells.teammission.domain.rs;

import java.io.Serializable;
import java.util.Date;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * 用户和评论的值对象
 * @author Administrator
 *
 */
public class CommentUserVos implements Serializable {
	private static final long serialVersionUID = -6789450271108407956L;
	private Integer commentId;  
	private Integer parentId;  //父级别的评论
	private Integer userId;   //回复人的Id
	private String userIcon;  //用户头像
	private String realName;  //用户的真实姓名
	private Date replayTime;  //回复的时间
	private String content;  //评论的内容
	private String toRealName;  //被回复的人的名字
	private Integer toUserId;  //被回复的用户Id
	private Integer likeCount;
	private Integer disLikeCount;
	@Override
	public String toString() {
		return "CommentUserVos [commentId=" + commentId + ", parentId="
				+ parentId + ", userId=" + userId + ", userIcon=" + userIcon
				+ ", realName=" + realName + ", replayTime=" + replayTime
				+ ", content=" + content + ", toRealName=" + toRealName
				+ ", toUserId=" + toUserId + ", likeCount=" + likeCount
				+ ", disLikeCount=" + disLikeCount + "]";
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Date getReplayTime() {
		return replayTime;
	}
	public void setReplayTime(Date replayTime) {
		this.replayTime = replayTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getToRealName() {
		return toRealName;
	}
	public void setToRealName(String toRealName) {
		this.toRealName = toRealName;
	}
	public Integer getToUserId() {
		return toUserId;
	}
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public Integer getDisLikeCount() {
		return disLikeCount;
	}
	public void setDisLikeCount(Integer disLikeCount) {
		this.disLikeCount = disLikeCount;
	}
	
	
	
}

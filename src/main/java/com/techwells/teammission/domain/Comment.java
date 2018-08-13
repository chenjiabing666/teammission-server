package com.techwells.teammission.domain;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
	private static final long serialVersionUID = -4747881837834161823L;

	private Integer commentId;

    private Integer userId;

    private Integer rowId;

    private Integer parentId;

    private Integer toUserId;

    private Integer weeklyId;

    private Date createdDate;

    private Integer activated;

    private Integer deleted;

    private Date updateDate;

    private String content;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getWeeklyId() {
        return weeklyId;
    }

    public void setWeeklyId(Integer weeklyId) {
        this.weeklyId = weeklyId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userId=" + userId
				+ ", rowId=" + rowId + ", parentId=" + parentId + ", toUserId="
				+ toUserId + ", weeklyId=" + weeklyId + ", createdDate="
				+ createdDate + ", activated=" + activated + ", deleted="
				+ deleted + ", updateDate=" + updateDate + ", content="
				+ content + "]";
	}
    
    
}
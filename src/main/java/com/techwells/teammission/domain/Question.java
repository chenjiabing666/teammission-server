package com.techwells.teammission.domain;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable{
	private static final long serialVersionUID = -2598705652059125751L;

	private Integer questionId;

    private String repyTime;

    private String title;

    private Integer deleted;

    private Integer visibility;

    private String showUsersId;

    private Integer replyWho;

    private String replyUsersId;

    private Integer remind;

    private Integer remindTime;

    private Date createdDate;

    private Date updateDate;

    private String description;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getRepyTime() {
        return repyTime;
    }

    public void setRepyTime(String repyTime) {
        this.repyTime = repyTime == null ? null : repyTime.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getShowUsersId() {
        return showUsersId;
    }

    public void setShowUsersId(String showUsersId) {
        this.showUsersId = showUsersId == null ? null : showUsersId.trim();
    }

    public Integer getReplyWho() {
        return replyWho;
    }

    public void setReplyWho(Integer replyWho) {
        this.replyWho = replyWho;
    }

    public String getReplyUsersId() {
        return replyUsersId;
    }

    public void setReplyUsersId(String replyUsersId) {
        this.replyUsersId = replyUsersId == null ? null : replyUsersId.trim();
    }

    public Integer getRemind() {
        return remind;
    }

    public void setRemind(Integer remind) {
        this.remind = remind;
    }

    public Integer getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Integer remindTime) {
        this.remindTime = remindTime;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
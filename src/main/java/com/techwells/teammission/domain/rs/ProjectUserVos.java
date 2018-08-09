package com.techwells.teammission.domain.rs;

import java.io.Serializable;

/**
 * 用户和项目的的详细信息
 * @author Administrator
 *
 */
public class ProjectUserVos implements Serializable {
	private static final long serialVersionUID = 6049970000113118292L;
	private Integer projectId;
	private Integer userId;
	private String realName;
	private String projectName;
	private Integer publicty;   //项目公开性
	private String introduction;   //项目简介
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getPublicty() {
		return publicty;
	}
	public void setPublicty(Integer publicty) {
		this.publicty = publicty;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
}

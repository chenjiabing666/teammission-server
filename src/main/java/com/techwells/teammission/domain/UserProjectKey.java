package com.techwells.teammission.domain;

import java.io.Serializable;

public class UserProjectKey implements Serializable {
    private Integer userId;

    private Integer projectId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
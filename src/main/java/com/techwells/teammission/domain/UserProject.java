package com.techwells.teammission.domain;

import java.io.Serializable;
import java.util.Date;

public class UserProject extends UserProjectKey implements Serializable {
	private static final long serialVersionUID = 6918551377276150368L;

	private Date createdDate;

    private Integer power;

    private Integer activated;

    private Integer deleted;

    private Date updatedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
package com.techwells.teammission.domain;

import java.io.Serializable;

public class ProjectWithBLOBs extends Project implements Serializable {
	private static final long serialVersionUID = 2461602742688401472L;

	private String introduction;

    private String detail;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}
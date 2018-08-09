package com.techwells.teammission.dao;

import java.util.List;

import com.techwells.teammission.domain.Project;
import com.techwells.teammission.domain.ProjectWithBLOBs;
import com.techwells.teammission.domain.User;

public interface ProjectMapper {
    int deleteByPrimaryKey(Integer projectId);

    int insert(ProjectWithBLOBs record);

    int insertSelective(ProjectWithBLOBs record);

    ProjectWithBLOBs selectByPrimaryKey(Integer projectId);

    int updateByPrimaryKeySelective(ProjectWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProjectWithBLOBs record);

    int updateByPrimaryKey(Project record);
    
    /**
     * 获取指定项目的所有成员
     * @param projectId  项目Id
     * @return
     */
    List<User> selectUserByProjectId(Integer projectId);
}
package com.techwells.teammission.dao;

import com.techwells.teammission.domain.UserProject;
import com.techwells.teammission.domain.UserProjectKey;
import com.techwells.teammission.domain.rs.ProjectUserVos;

public interface UserProjectMapper {
    int deleteByPrimaryKey(UserProjectKey key);

    int insert(UserProject record);

    int insertSelective(UserProject record);

    UserProject selectByPrimaryKey(UserProjectKey key);

    int updateByPrimaryKeySelective(UserProject record);

    int updateByPrimaryKey(UserProject record);
    
    /**
     * 根据项目id获取项目的详细信息
     * @param projectId
     * @return
     */
    ProjectUserVos selectProjectUserVosByProjectId(Integer projectId);
    
}
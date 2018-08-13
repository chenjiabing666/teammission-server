package com.techwells.teammission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.techwells.teammission.domain.ProjectDynamic;

public interface ProjectDynamicMapper {
    int deleteByPrimaryKey(Integer dynamicId);

    int insert(ProjectDynamic record);

    int insertSelective(ProjectDynamic record);

    ProjectDynamic selectByPrimaryKey(Integer dynamicId);

    int updateByPrimaryKeySelective(ProjectDynamic record);

    int updateByPrimaryKey(ProjectDynamic record);
    
    /**
     * 获取项目的动态
     * @param projectId  项目id
     * @param num  数量
     * @return
     */
    List<ProjectDynamic> selectDynamicsByProjectId(@Param("projectId")Integer projectId,@Param("num")Integer num);
}
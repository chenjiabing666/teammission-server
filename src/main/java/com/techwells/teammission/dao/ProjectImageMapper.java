package com.techwells.teammission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.techwells.teammission.domain.ProjectImage;

public interface ProjectImageMapper {
    int deleteByPrimaryKey(Integer imageId);

    int insert(ProjectImage record);

    int insertSelective(ProjectImage record);

    ProjectImage selectByPrimaryKey(Integer imageId);

    int updateByPrimaryKeySelective(ProjectImage record);

    int updateByPrimaryKey(ProjectImage record);
    
    /**
     * 批量插入图片数据
     * @param list 图片列表
     * @return
     */
    int insertImageBatch(List<ProjectImage> list);
    
    /**
     * 根据项目id获取图片
     * @param projectId  项目Id
     * @return 
     */
    List<ProjectImage> selectImagesByProjectId(Integer projectId);
    
    
    
}
package com.techwells.teammission.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.ProjectImage;

@Transactional
public interface ProjectImageService {
	
	/**
	 * 批量添加图片
	 * @param projectImages 项目的图片数据
	 * @param userId 便于获取发布人的信息，用于发布动态
	 */
	Object uploadProjectImageBatch(List<ProjectImage> projectImages,Integer userId)throws Exception;
	
	/**
	 * 根据项目Id获取所有的图片
	 * @param projectId
	 * @return
	 */
	Object getImagesByProjectId(Integer projectId)throws Exception;
	
	
	/**
	 * 根据imageId删除图片
	 * @param imageId  图片Id
	 * @param projectId  项目Id 这个值是便于获取缓存中的值，因为缓存中的数据field就是projectId
	 * @return
	 * @throws Exception
	 */
	Object deleteImageByImageId(Integer imageId,Integer projectId)throws Exception;
	
	
	
	
	
	
}

package com.techwells.teammission.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.ProjectImage;

@Transactional
public interface ProjectImageService {
	
	/**
	 * 批量添加图片
	 * @param projectImages
	 */
	Object uploadProjectImageBatch(List<ProjectImage> projectImages)throws Exception;
	
	
}

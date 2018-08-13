package com.techwells.teammission.service;

import org.springframework.transaction.annotation.Transactional;

import com.techwells.teammission.domain.Interface;

@Transactional
public interface InterfaceService {
	/**
	 * 添加接口
	 * @param imageId  图片Id
	 * @param face   接口
	 * @return  结果集
	 * @throws Exception
	 */
	Object addInterface(Interface face)throws Exception;
	
	/**
	 * 根据页面Id获取该页面对应的所有接口
	 * @param imageId  图片Id
	 * @return  结果集
	 */
	Object getInterface(Integer imageId)throws Exception;
	
	/**
	 * 根据接口Id删除接口信息
	 * @param faceId  接口Id
	 * @param imageId 页面Id，为了删除缓存中的信息，因此需要传这个参数
	 * @return
	 * @throws Exception
	 */
	Object deleteInterface(Integer faceId,Integer imageId) throws Exception;
}

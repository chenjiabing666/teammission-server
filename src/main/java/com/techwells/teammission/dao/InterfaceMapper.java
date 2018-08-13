package com.techwells.teammission.dao;

import java.util.List;

import com.techwells.teammission.domain.Interface;

public interface InterfaceMapper {
    int deleteByPrimaryKey(Integer interfaceId);

    int insert(Interface record);

    int insertSelective(Interface record);

    Interface selectByPrimaryKey(Integer interfaceId);

    int updateByPrimaryKeySelective(Interface record);

    int updateByPrimaryKeyWithBLOBs(Interface record);

    int updateByPrimaryKey(Interface record);
    
    /**
     * 根据ImageId获取所有的接口信息
     * @param imageId
     * @return
     */
    List<Interface> selectInterFaceByImageId(Integer imageId);
    
    /**
     * 根据页面Id删除接口
     * @param imageId 页面Id
     * @return
     */
    int deleteFaceByImageId(Integer imageId);
}
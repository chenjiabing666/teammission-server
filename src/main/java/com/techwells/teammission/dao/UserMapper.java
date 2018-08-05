package com.techwells.teammission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.techwells.teammission.domain.User;
import com.techwells.teammission.util.PagingTool;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int deleteBatchByUserIds(@Param("userIds")String[] userIds);
    
    /**
     * 分页获取数据
     * @param pagingTool 分页的封装对象
     * @return
     */
    List<User> selectUserList(PagingTool pagingTool);
}
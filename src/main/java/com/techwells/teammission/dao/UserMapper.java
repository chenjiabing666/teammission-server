package com.techwells.teammission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.techwells.teammission.domain.User;
import com.techwells.teammission.util.PagingTool;

public interface UserMapper {
	int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int deleteBatchByUserIds(@Param("userIds")String[] userIds);
    
    
    /**
     * 根据手机号码获取用户信息
     * @param mobile
     * @return
     */
    User selectUserByMobile(String mobile);
    
    /**
     * 根据邮箱获取用户信息
     * @param email
     * @return
     */
    User selectUserByEmail(String email);
    
    /**
     * 分页获取数据
     * @param pagingTool 分页的封装对象
     * @return
     */
    List<User> selectUserList(PagingTool pagingTool);
    
    
    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    User selectUserByUserName(String userName);
    
}
package com.techwells.teammission.dao;

import com.techwells.teammission.domain.Question;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer questionId);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer questionId);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);
}
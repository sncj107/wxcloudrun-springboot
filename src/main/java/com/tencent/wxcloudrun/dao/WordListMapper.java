package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.WordList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 词汇列表 Mapper 接口
 */
@Mapper
public interface WordListMapper {

    /**
     * 插入词汇列表
     */
    int insert(WordList wordList);

    /**
     * 根据 ID 删除词汇列表
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新词汇列表
     */
    int update(WordList wordList);

    /**
     * 根据 ID 查询词汇列表
     */
    WordList getById(@Param("id") Integer id);

    /**
     * 根据用户 openId 查询所有词汇列表
     */
    List<WordList> getByOpenId(@Param("openId") String openId);

    /**
     * 检查词汇列表是否属于指定用户
     */
    WordList checkOwnership(@Param("id") Integer id, @Param("openId") String openId);
}

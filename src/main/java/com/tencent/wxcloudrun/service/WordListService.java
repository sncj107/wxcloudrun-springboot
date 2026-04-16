package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.CreateWordListRequest;
import com.tencent.wxcloudrun.dto.UpdateWordListRequest;
import com.tencent.wxcloudrun.model.WordList;

import java.util.List;

/**
 * 词汇列表服务接口
 */
public interface WordListService {

    /**
     * 创建词汇列表
     */
    WordList createWordList(String openId, CreateWordListRequest request);

    /**
     * 删除词汇列表
     */
    void deleteWordList(String openId, Integer id);

    /**
     * 更新词汇列表
     */
    WordList updateWordList(String openId, UpdateWordListRequest request);

    /**
     * 查询词汇列表详情
     */
    WordList getWordListById(String openId, Integer id);

    /**
     * 查询用户的所有词汇列表
     */
    List<WordList> getWordListsByOpenId(String openId);

    /**
     * 添加词汇到列表
     */
    WordList addWord(String openId, Integer id, String word);

    /**
     * 从列表中删除词汇
     */
    WordList removeWord(String openId, Integer id, String word);

    
    }

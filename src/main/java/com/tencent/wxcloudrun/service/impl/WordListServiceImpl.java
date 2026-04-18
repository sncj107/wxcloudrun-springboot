package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.WordListMapper;
import com.tencent.wxcloudrun.dto.CreateWordListRequest;
import com.tencent.wxcloudrun.dto.UpdateWordListRequest;
import com.tencent.wxcloudrun.model.WordList;
import com.tencent.wxcloudrun.service.WordListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 词汇列表服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WordListServiceImpl implements WordListService {

    private final WordListMapper wordListMapper;

    @Override
    public WordList createWordList(String openId, CreateWordListRequest request) {
        WordList wordList = new WordList();
        wordList.setName(request.getName());
        wordList.setWords(request.getWords());
        wordList.setOpenId(openId);
        
        wordListMapper.insert(wordList);
        return wordList;
    }

    @Override
    public void deleteWordList(String openId, Integer id) {
        // 检查所有权
        WordList wordList = wordListMapper.checkOwnership(id, openId);
        if (wordList == null) {
            throw new RuntimeException("词汇列表不存在或无权操作");
        }
        
        wordListMapper.deleteById(id);
    }

    @Override
    public WordList updateWordList(String openId, UpdateWordListRequest request) {
        // 检查所有权
        WordList wordList = wordListMapper.checkOwnership(request.getId(), openId);
        log.info("更新词汇列表权限检查：id: {}, openId: {}, wordList: {}", request.getId(), openId, wordList);
        if (wordList == null) {
            log.info("更新词汇列表权限检查失败，词汇列表不存在或无权操作");
            throw new RuntimeException("词汇列表不存在或无权操作");
        }
        
        wordList.setName(request.getName());
        wordList.setWords(request.getWords());
        
        wordListMapper.update(wordList);
        return wordList;
    }

    @Override
    public WordList getWordListById(String openId, Integer id) {
        // 检查所有权
        WordList wordList = wordListMapper.checkOwnership(id, openId);
        if (wordList == null) {
            throw new RuntimeException("词汇列表不存在或无权操作");
        }
        
        return wordList;
    }

    @Override
    public List<WordList> getWordListsByOpenId(String openId) {
        return wordListMapper.getByOpenId(openId);
    }

    @Override
    public WordList addWord(String openId, Integer id, String word) {
        // 检查所有权
        WordList wordList = wordListMapper.checkOwnership(id, openId);
        if (wordList == null) {
            throw new RuntimeException("词汇列表不存在或无权操作");
        }
        
        String words = wordList.getWords();
        if (words == null || words.isEmpty()) {
            words = word;
        } else {
            words = words + "," + word;
        }
        wordList.setWords(words);
        
        wordListMapper.update(wordList);
        return wordList;
    }

    @Override
    public WordList removeWord(String openId, Integer id, String word) {
        // 检查所有权
        WordList wordList = wordListMapper.checkOwnership(id, openId);
        if (wordList == null) {
            throw new RuntimeException("词汇列表不存在或无权操作");
        }
        
        String words = wordList.getWords();
        if (words != null && !words.isEmpty()) {
            String[] wordArray = words.split(",");
            StringBuilder newWords = new StringBuilder();
            for (String w : wordArray) {
                if (!w.equals(word)) {
                    if (newWords.length() > 0) {
                        newWords.append(",");
                    }
                    newWords.append(w);
                }
            }
            wordList.setWords(newWords.toString());
            wordListMapper.update(wordList);
        }
        return wordList;
    }


}

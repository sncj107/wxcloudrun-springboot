package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CreateWordListRequest;
import com.tencent.wxcloudrun.dto.UpdateWordListRequest;
import com.tencent.wxcloudrun.model.WordList;
import com.tencent.wxcloudrun.service.WordListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 词汇列表控制器
 */
@RestController
@RequestMapping("/api/wordlists")
@RequiredArgsConstructor
public class WordListController {

    private final WordListService wordListService;

    /**
     * 获取用户 openId(从请求头或会话中)
     */
    private String getOpenId(HttpServletRequest request) {
        // 从请求头获取 openId，实际项目中可能从 token 解析
        String openId = request.getHeader("X-WX-OPENID");
        if (openId == null || openId.isEmpty()) {
            throw new RuntimeException("未找到用户 openId");
        }
        return openId;
    }

    /**
     * 创建词汇列表
     */
    @PostMapping
    public ApiResponse createWordList(@RequestBody CreateWordListRequest request,
                                                 HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            WordList wordList = wordListService.createWordList(openId, request);
            return ApiResponse.ok(wordList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除词汇列表
     */
    @DeleteMapping("/{id}")
    public ApiResponse deleteWordList(@PathVariable Integer id,
                                             HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            wordListService.deleteWordList(openId, id);
            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新词汇列表
     */
    @PutMapping("/{id}")
    public ApiResponse updateWordList(@PathVariable Integer id,
                                                 @RequestBody UpdateWordListRequest request,
                                                 HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            WordList wordList = wordListService.updateWordList(openId, request);
            return ApiResponse.ok(wordList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询词汇列表详情
     */
    @GetMapping("/{id}")
    public ApiResponse getWordListById(@PathVariable Integer id,
                                                   HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            WordList wordList = wordListService.getWordListById(openId, id);
            return ApiResponse.ok(wordList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询用户的所有词汇列表
     */
    @GetMapping("/list")
    public ApiResponse getWordLists(HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            List<WordList> wordLists = wordListService.getWordListsByOpenId(openId);
            return ApiResponse.ok(wordLists);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 添加词汇到列表
     */
    @PostMapping("/{id}/words")
    public ApiResponse addWord(@PathVariable Integer id,
                                          @RequestParam String word,
                                          HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            WordList wordList = wordListService.addWord(openId, id, word);
            return ApiResponse.ok(wordList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 从列表中删除词汇
     */
    @DeleteMapping("/{id}/words")
    public ApiResponse removeWord(@PathVariable Integer id,
                                             @RequestParam String word,
                                             HttpServletRequest httpRequest) {
        try {
            String openId = getOpenId(httpRequest);
            WordList wordList = wordListService.removeWord(openId, id, word);
            return ApiResponse.ok(wordList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    
    }

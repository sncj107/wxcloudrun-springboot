package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.TextToAudioRequest;
import com.tencent.wxcloudrun.service.BaiduAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 百度AI 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/baidu")
public class BaiduAIController {

    @Autowired
    private BaiduAIService baiduAIService;

    /**
     * 文本转语音 - 返回音频文件
     */
    @PostMapping("/text2audio")
    public ResponseEntity<byte[]> textToAudio(@RequestBody TextToAudioRequest request) {
        try {
            // 参数校验
            if (request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getCuid() == null || request.getCuid().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            log.info("文本转语音请求：text={}, cuid={}", request.getText(), request.getCuid());
            
            // 调用服务
            byte[] audioData = baiduAIService.textToAudio(request.getText(), request.getCuid());
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/mp3"));
            headers.setContentLength(audioData.length);
            
            return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("文本转语音失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ApiResponse test() {
        return ApiResponse.ok("百度AI 服务正常运行");
    }
}

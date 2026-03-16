package com.tencent.wxcloudrun.service;

/**
 * 百度AI 服务接口
 */
public interface BaiduAIService {
    
    /**
     * 获取访问令牌
     * @return access_token
     */
    String getAccessToken();
    
    /**
     * 文本转语音
     * @param text 文本内容
     * @param cuid 用户唯一标识
     * @return MP3 音频字节数组
     */
    byte[] textToAudio(String text, String cuid);
}

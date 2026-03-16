package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.dto.BaiduAuthResponse;
import com.tencent.wxcloudrun.service.BaiduAIService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 百度 AI 服务实现类
 */
@Slf4j
@Service
public class BaiduAIServiceImpl implements BaiduAIService {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    @Value("${baidu.ai.api-key}")
    private String apiKey;

    @Value("${baidu.ai.secret-key}")
    private String secretKey;

    @Value("${baidu.ai.oauth-url}")
    private String oauthUrl;

    @Value("${baidu.ai.text2audio-url}")
    private String text2AudioUrl;

    /**
     * 缓存 access_token，减少重复请求
     */
    private String cachedAccessToken;
    private long tokenExpireTime;

    @Override
    public String getAccessToken() {
        // 检查缓存的 token 是否有效
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return cachedAccessToken;
        }

        try {
            String url = String.format("%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                    oauthUrl, apiKey, secretKey);

            RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
            
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseBody = response.body().string();
            
            log.info("百度 AI 鉴权响应：{}", responseBody);
            
            BaiduAuthResponse authResponse = JSON.parseObject(responseBody, BaiduAuthResponse.class);
            
            if (authResponse.getAccessToken() != null) {
                this.cachedAccessToken = authResponse.getAccessToken();
                // 提前 5 分钟过期，确保不会使用即将过期的 token
                this.tokenExpireTime = System.currentTimeMillis() + 
                        (authResponse.getExpiresIn() - 300) * 1000L;
                return authResponse.getAccessToken();
            } else {
                log.error("获取 access_token 失败：{}", responseBody);
                throw new RuntimeException("获取 access_token 失败：" + responseBody);
            }
        } catch (IOException e) {
            log.error("获取 access_token 异常", e);
            throw new RuntimeException("获取 access_token 异常", e);
        }
    }

    @Override
    public byte[] textToAudio(String text, String cuid) {
        try {
            String accessToken = getAccessToken();
            
            // 构建请求参数
            HttpUrl httpUrl = HttpUrl.parse(text2AudioUrl).newBuilder()
                    .addQueryParameter("tex", text)
                    .addQueryParameter("cuid", cuid)
                    .addQueryParameter("ctp", "1")
                    .addQueryParameter("per", "3")
                    .addQueryParameter("lan", "zh")
                    .addQueryParameter("aue", "3")
                    .addQueryParameter("tok", accessToken)
                    .build();

            RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
            
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            
            // 检查响应头判断是否成功
            String contentType = response.header("Content-Type");
            if (contentType != null && contentType.startsWith("audio")) {
                // 合成成功，返回音频文件
                return response.body().bytes();
            } else {
                // 合成失败，返回错误信息
                String errorBody = response.body().string();
                log.error("文本转语音失败：{}", errorBody);
                throw new RuntimeException("文本转语音失败：" + errorBody);
            }
        } catch (IOException e) {
            log.error("文本转语音异常", e);
            throw new RuntimeException("文本转语音异常", e);
        }
    }
}

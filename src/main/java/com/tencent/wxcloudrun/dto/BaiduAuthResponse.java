package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 百度AI 鉴权响应 DTO
 */
@Data
public class BaiduAuthResponse {
    private String access_token;
    private String refresh_token;
    private Integer expires_in;
    private String session_key;
    private String session_secret;
    private String scope;
    
    // 手动添加 getter 方法以解决 Lombok 问题
    public String getAccessToken() {
        return access_token;
    }
    
    public Integer getExpiresIn() {
        return expires_in;
    }
}

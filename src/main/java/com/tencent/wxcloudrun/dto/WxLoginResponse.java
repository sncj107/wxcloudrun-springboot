package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 微信登录响应 DTO
 */
@Data
public class WxLoginResponse {
    /**
     * 用户在小程序的 OpenID
     */
    private String openId;

    /**
     * 会话密钥 session_key
     */
    private String sessionKey;

    /**
     * 用户在开放平台的 UnionID（如果有）
     */
    private String unionId;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
        private String nickname;
        private String avatarUrl;
        private Integer gender;
        private String language;
        private String city;
        private String province;
        private String country;
    }
}

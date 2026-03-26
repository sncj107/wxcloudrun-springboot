package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 更新用户信息请求 DTO
 */
@Data
public class UpdateUserInfoRequest {
    /**
     * 微信用户唯一标识 openId（必填）
     */
    private String openId;

    /**
     * 用户昵称（可选）
     */
    private String nickname;

    /**
     * 用户头像 URL（可选）
     */
    private String avatarUrl;

    /**
     * 性别 0-未知 1-男 2-女（可选）
     */
    private Integer gender;

    /**
     * 语言（可选）
     */
    private String language;

    /**
     * 城市（可选）
     */
    private String city;

    /**
     * 省份（可选）
     */
    private String province;

    /**
     * 国家（可选）
     */
    private String country;
}

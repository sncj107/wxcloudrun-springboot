package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 微信用户信息实体类
 */
@Data
public class User implements Serializable {

    private Integer id;

    /**
     * 微信用户唯一标识
     */
    private String openId;

    /**
     * 微信开放平台唯一标识
     */
    private String unionId;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像 URL
     */
    private String avatarUrl;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 语言
     */
    private String language;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

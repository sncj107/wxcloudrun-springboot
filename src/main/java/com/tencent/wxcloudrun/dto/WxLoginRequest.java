package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 微信登录请求 DTO
 */
@Data
public class WxLoginRequest {
    /**
     * 小程序登录凭证 code（必填）
     * 通过 wx.login() 获取
     */
    private String code;
}

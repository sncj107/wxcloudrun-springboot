package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.UpdateUserInfoRequest;
import com.tencent.wxcloudrun.dto.WxLoginRequest;
import com.tencent.wxcloudrun.dto.WxLoginResponse;
import com.tencent.wxcloudrun.model.User;

/**
 * 微信服务接口
 */
public interface WxService {

    /**
     * 微信小程序登录
     * @param request 登录请求
     * @return 登录响应
     */
    WxLoginResponse login(WxLoginRequest request);

    /**
     * 获取微信访问令牌
     * @return access_token
     */
    String getAccessToken();

    /**
     * 更新用户信息
     * @param request 更新请求（包含 openId 和用户信息）
     * @return 更新后的用户信息
     */
    User updateUserInfo(UpdateUserInfoRequest request);
}

package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.UpdateUserInfoRequest;
import com.tencent.wxcloudrun.dto.WxLoginRequest;
import com.tencent.wxcloudrun.dto.WxLoginResponse;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.WxService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 微信服务实现类
 */
@Slf4j
@Service
public class WxServiceImpl implements WxService {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 微信小程序 appId（必填）
     */
    @Value("${wx.miniapp.appid}")
    private String appId;

    /**
     * 微信小程序 appSecret（必填）
     */
    @Value("${wx.miniapp.secret}")
    private String appSecret;

    /**
     * 微信登录凭证校验接口
     */
    private static final String WX_AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 缓存 access_token，减少重复请求
     */
    private String cachedAccessToken;
    private long tokenExpireTime;

    @Resource
    private UserMapper userMapper;

    @Override
    public WxLoginResponse login(WxLoginRequest request) {
        try {
            log.info("微信登录请求：code={}", request.getCode());

            // 1. 调用微信接口获取 openId 和 session_key
            String authUrl = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    WX_AUTH_URL, appId, appSecret, request.getCode());

            Request wxRequest = new Request.Builder()
                    .url(authUrl)
                    .method("GET", null)
                    .build();

            Response response = HTTP_CLIENT.newCall(wxRequest).execute();
            String responseBody = response.body().string();

            log.info("微信鉴权响应：{}", responseBody);

            JSONObject authResult = JSON.parseObject(responseBody);

            // 检查是否有错误
            if (authResult.containsKey("errcode")) {
                String errMsg = authResult.getString("errmsg");
                log.error("微信登录失败：{}", errMsg);
                throw new RuntimeException("微信登录失败：" + errMsg);
            }

            // 解析 openId 和 session_key
            String openId = authResult.getString("openid");
            String sessionKey = authResult.getString("session_key");
            String unionId = authResult.getString("unionid");

            if (openId == null || sessionKey == null) {
                throw new RuntimeException("获取微信用户信息失败");
            }

            // 2. 查询用户是否已存在
            User existingUser = userMapper.findByOpenId(openId);

            if (existingUser != null) {
                // 用户已存在，更新 session_key 和用户信息
                existingUser.setSessionKey(sessionKey);
                existingUser.setUnionId(unionId);
                existingUser.setUpdatedAt(LocalDateTime.now());
                userMapper.update(existingUser);

                log.info("用户已存在，更新成功：openId={}", openId);
            } else {
                // 用户不存在，创建新用户
                User newUser = new User();
                newUser.setOpenId(openId);
                newUser.setSessionKey(sessionKey);
                newUser.setUnionId(unionId);
                newUser.setCreatedAt(LocalDateTime.now());
                newUser.setUpdatedAt(LocalDateTime.now());

                userMapper.insert(newUser);

                log.info("新用户创建成功：openId={}", openId);
            }

            // 3. 构建响应
            WxLoginResponse wxResponse = new WxLoginResponse();
            wxResponse.setOpenId(openId);
            wxResponse.setSessionKey(sessionKey);
            wxResponse.setUnionId(unionId);

            // 重新从数据库查询最新的用户信息
            User dbUser = userMapper.findByOpenId(openId);
            
            // 只有当数据库中有用户信息时才设置 userInfo
            if (dbUser != null && hasUserInfo(dbUser)) {
                WxLoginResponse.UserInfo userInfo = new WxLoginResponse.UserInfo();
                userInfo.setNickname(dbUser.getNickname());
                userInfo.setAvatarUrl(dbUser.getAvatarUrl());
                userInfo.setGender(dbUser.getGender());
                userInfo.setLanguage(dbUser.getLanguage());
                userInfo.setCity(dbUser.getCity());
                userInfo.setProvince(dbUser.getProvince());
                userInfo.setCountry(dbUser.getCountry());
                wxResponse.setUserInfo(userInfo);
            }

            return wxResponse;

        } catch (IOException e) {
            log.error("微信登录异常", e);
            throw new RuntimeException("微信登录异常：" + e.getMessage());
        }
    }

    @Override
    public String getAccessToken() {
        // 检查缓存的 token 是否有效
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return cachedAccessToken;
        }

        try {
            // 小程序获取 access_token 的接口
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                    appId, appSecret);

            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            String responseBody = response.body().string();

            log.info("微信 access_token 响应：{}", responseBody);

            JSONObject result = JSON.parseObject(responseBody);

            if (result.containsKey("access_token")) {
                this.cachedAccessToken = result.getString("access_token");
                // access_token 有效期为 7200 秒，提前 5 分钟刷新
                this.tokenExpireTime = System.currentTimeMillis() + (7200 - 300) * 1000L;
                return this.cachedAccessToken;
            } else {
                String errMsg = result.getString("errmsg");
                log.error("获取 access_token 失败：{}", errMsg);
                throw new RuntimeException("获取 access_token 失败：" + errMsg);
            }
        } catch (IOException e) {
            log.error("获取 access_token 异常", e);
            throw new RuntimeException("获取 access_token 异常：" + e.getMessage());
        }
    }

    /**
     * 判断用户是否有基本信息
     * @param user 用户对象
     * @return true-有信息，false-无信息
     */
    private boolean hasUserInfo(User user) {
        return user.getNickname() != null || 
               user.getAvatarUrl() != null || 
               user.getGender() != null || 
               user.getLanguage() != null || 
               user.getCity() != null || 
               user.getProvince() != null || 
               user.getCountry() != null;
    }

    @Override
    public User updateUserInfo(UpdateUserInfoRequest request) {
        // 参数校验
        if (request.getOpenId() == null || request.getOpenId().isEmpty()) {
            throw new IllegalArgumentException("openId 不能为空");
        }

        log.info("更新用户信息请求：openId={}", request.getOpenId());

        // 查询用户是否存在
        User existingUser = userMapper.findByOpenId(request.getOpenId());

        if (existingUser == null) {
            throw new RuntimeException("用户不存在：openId=" + request.getOpenId());
        }

        // 更新用户信息（只更新非空字段）
        if (request.getNickname() != null) {
            existingUser.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getGender() != null) {
            existingUser.setGender(request.getGender());
        }
        if (request.getLanguage() != null) {
            existingUser.setLanguage(request.getLanguage());
        }
        if (request.getCity() != null) {
            existingUser.setCity(request.getCity());
        }
        if (request.getProvince() != null) {
            existingUser.setProvince(request.getProvince());
        }
        if (request.getCountry() != null) {
            existingUser.setCountry(request.getCountry());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userMapper.update(existingUser);

        log.info("用户信息更新成功：openId={}", request.getOpenId());

        return existingUser;
    }
}

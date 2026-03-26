package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UpdateUserInfoRequest;
import com.tencent.wxcloudrun.dto.WxLoginRequest;
import com.tencent.wxcloudrun.dto.WxLoginResponse;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 微信服务 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/wx")
public class WxController {

    @Resource
    private WxService wxService;

    /**
     * 微信小程序登录接口
     * 
     * 流程说明：
     * 1. 前端调用 wx.login() 获取临时登录凭证 code
     * 2. 前端将 code 发送到该接口
     * 3. 后端向微信服务器请求换取 openId 和 sessionKey
     * 4. 查询数据库中的用户信息并返回
     * 
     * @param request 登录请求（只包含 code）
     * @return 登录响应（包含 openId、sessionKey 和用户信息）
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody WxLoginRequest request) {
        try {
            // 参数校验
            if (request.getCode() == null || request.getCode().isEmpty()) {
                return ApiResponse.error("code 不能为空");
            }

            log.info("收到微信登录请求，code={}", request.getCode());

            // 调用微信登录服务
            WxLoginResponse response = wxService.login(request);

            log.info("微信登录成功，openId={}", response.getOpenId());

            return ApiResponse.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
            return ApiResponse.error("参数错误：" + e.getMessage());
        } catch (Exception e) {
            log.error("微信登录失败", e);
            return ApiResponse.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ApiResponse test() {
        return ApiResponse.ok("微信服务正常运行");
    }

    /**
     * 获取 access_token（用于其他微信 API 调用）
     */
    @GetMapping("/token")
    public ApiResponse getToken() {
        try {
            String accessToken = wxService.getAccessToken();
            return ApiResponse.ok(accessToken);
        } catch (Exception e) {
            log.error("获取 access_token 失败", e);
            return ApiResponse.error("获取 token 失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * 
     * @param request 更新请求（包含 openId 和用户信息字段）
     * @return 更新后的用户信息
     */
    @PostMapping("/user/update")
    public ApiResponse updateUser(@RequestBody UpdateUserInfoRequest request) {
        try {
            // 参数校验
            if (request.getOpenId() == null || request.getOpenId().isEmpty()) {
                return ApiResponse.error("openId 不能为空");
            }

            log.info("收到更新用户信息请求，openId={}", request.getOpenId());

            // 调用服务更新用户信息
            User updatedUser = wxService.updateUserInfo(request);

            log.info("用户信息更新成功，openId={}", request.getOpenId());

            return ApiResponse.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.error("参数错误", e);
            return ApiResponse.error("参数错误：" + e.getMessage());
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return ApiResponse.error("更新失败：" + e.getMessage());
        }
    }
}

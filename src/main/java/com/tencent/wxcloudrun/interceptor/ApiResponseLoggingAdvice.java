package com.tencent.wxcloudrun.interceptor;

import com.alibaba.fastjson.JSON;
import com.tencent.wxcloudrun.config.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * API响应日志拦截器
 * 专门处理ApiResponse类型的返回值日志记录
 */
@Slf4j
@ControllerAdvice
@Component
public class ApiResponseLoggingAdvice implements ResponseBodyAdvice<Object> {

    @Value("${logging.api.enabled:false}")
    private boolean loggingEnabled;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 只处理ApiResponse类型的返回值
        return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!loggingEnabled || body == null || !(body instanceof ApiResponse)) {
            return body;
        }

        try {
            ApiResponse apiResponse = (ApiResponse) body;

            // 获取当前请求信息
            HttpServletRequest servletRequest = getCurrentRequest(request);
            if (servletRequest != null) {
                // 记录完整的API调用日志（包含返回值）
                long endTime = System.currentTimeMillis();
                Long startTime = (Long) servletRequest.getAttribute("requestStartTime");
                long duration = startTime != null ? endTime - startTime : 0;

                // 获取请求参数
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> params = (java.util.Map<String, Object>) servletRequest.getAttribute("requestParams");

                // 获取X-WX-OPENID
                String openId = servletRequest.getHeader("X-WX-OPENID");

                // 获取客户端IP
                String clientIp = getClientIp(servletRequest);

                // 记录完整的API日志
                log.info("API调用完成 - 方法: {} URI: {} 客户端IP: {} 参数: {} 状态码: {} 错误信息: {} 返回数据: {} 响应时间: {}ms X-WX-OPENID: {}",
                        servletRequest.getMethod(),
                        servletRequest.getRequestURI(),
                        clientIp,
                        params != null ? JSON.toJSONString(params) : "{}",
                        apiResponse.getCode(),
                        apiResponse.getErrorMsg(),
                        apiResponse.getData() != null ? JSON.toJSONString(apiResponse.getData()) : "null",
                        duration,
                        openId);
            }
        } catch (Exception e) {
            log.warn("记录API返回值日志时发生异常", e);
        }

        return body;
    }

    /**
     * 从ServerHttpRequest获取HttpServletRequest
     */
    private HttpServletRequest getCurrentRequest(ServerHttpRequest request) {
        if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
            return ((org.springframework.http.server.ServletServerHttpRequest) request).getServletRequest();
        }
        return null;
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
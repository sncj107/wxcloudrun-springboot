package com.tencent.wxcloudrun.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * API请求日志拦截器
 * 记录请求参数、请求头等信息
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Value("${logging.api.enabled:false}")
    private boolean loggingEnabled;

    private static final String REQUEST_START_TIME = "requestStartTime";
    private static final String REQUEST_PARAMS = "requestParams";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!loggingEnabled) {
            return true;
        }

        long startTime = System.currentTimeMillis();
        request.setAttribute(REQUEST_START_TIME, startTime);

        // 记录请求参数
        Map<String, Object> params = new HashMap<>();

        // 获取查询参数
        Map<String, String[]> queryParams = request.getParameterMap();
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach((key, value) -> {
                if (value.length == 1) {
                    params.put(key, value[0]);
                } else {
                    params.put(key, value);
                }
            });
        }

        // 获取请求体参数 (仅对POST、PUT等方法的表单参数)
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String paramName = parameterNames.nextElement();
                    String[] paramValues = request.getParameterValues(paramName);
                    if (paramValues.length == 1) {
                        params.put(paramName, paramValues[0]);
                    } else {
                        params.put(paramName, paramValues);
                    }
                }
            }
        }

        request.setAttribute(REQUEST_PARAMS, params);

        // 获取请求头信息
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        // 特别记录X-WX-OPENID
        String openId = request.getHeader("X-WX-OPENID");

        // 记录请求开始日志
        log.info("API请求开始 - 方法: {} URI: {} 客户端IP: {} 参数: {} X-WX-OPENID: {}",
                request.getMethod(),
                request.getRequestURI(),
                getClientIp(request),
                JSON.toJSONString(params),
                openId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 这个方法现在不需要做任何事情，日志记录由ApiResponseLoggingAdvice处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 记录异常信息
        if (ex != null && loggingEnabled) {
            log.error("API异常 - 方法: {} URI: {} 异常信息: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage(),
                    ex);
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            return ip.split(",")[0];
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
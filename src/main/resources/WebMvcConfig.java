package com.tencent.wxcloudrun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射，确保/static/下的文件可以通过根路径访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setOrder(0); // 设置优先级，确保静态资源优先于视图解析
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 可选：将根路径映射到 index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}

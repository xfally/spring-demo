package com.example.demo.config;

import com.example.demo.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 认证拦截器配置
 *
 * @author pax
 */
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO: 这里`addPathPatterns`可以传入数组，我们可以在yml/properties中定义需要过滤的path。
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/customer/*", "/product/*", "/order/*", "/color/*");
    }
}

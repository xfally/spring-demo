package io.github.xfally.springdemo.config;

import io.github.xfally.springdemo.interceptor.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * 认证拦截器配置
 *
 * @author pax
 * @since 2020-03-19
 */
@Configuration
@ConditionalOnProperty(name = "auth.enable", havingValue = "true")
@Slf4j
public class AuthInterceptorConfig implements WebMvcConfigurer {
    @Value("${auth.list}")
    private String authListStr;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("authListStr={}", authListStr);
        if (StringUtils.isBlank(authListStr)) {
            log.warn("auth.list is blank!");
        }
        List<String> authList = Arrays.asList(authListStr.trim().split(","));
        log.debug("authList={}", authList);
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns(authList);
    }
}

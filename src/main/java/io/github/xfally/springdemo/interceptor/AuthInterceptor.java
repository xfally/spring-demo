package io.github.xfally.springdemo.interceptor;

import com.alibaba.fastjson.JSON;
import io.github.xfally.springdemo.common.model.UnifiedCodeEnum;
import io.github.xfally.springdemo.common.response.UnifiedException;
import io.github.xfally.springdemo.common.response.UnifiedResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证过滤器
 * <p>
 * 关于认证的设计建议：
 * <p>
 * 过滤器Filter和拦截器Interceptor都提供集中式的认证配置。
 * 相比于Filter，使用Interceptor处理认证信息更灵活，时机也更恰当。
 * 在AuthInterceptorConfig中配置需要拦截的path。
 * 更多参考Spring MVC全流程。
 * <p>
 * 如果想要使用非集中的认证，可以考虑使用自定义注解，配合AOP切面进行拦截和认证。
 * 比如定义一个`@AuthMe`，然后定义一个切面`@Aspect`，对有声明注解的控制器方法进行认证。
 * <p>
 * Interceptor加AOP也可以协同合作，
 * 比如Interceptor集中认证（从内部checkToken或外部统一认证服务），得到UserModel，并缓存到redis。
 * AOP处理声明自定义注解的方法，从redis提取UserModel信息，直接传参给控制器方法供业务使用。
 * <p>
 * 上面都是在一个业务软件里进行认证，对于一个分布式的大系统，往往会有一个到多个服务网关（API网关），
 * 那通常的做法是在网关处做统一认证，类似于上文拦截器Interceptor，得到UserModel，并缓存到redis...
 *
 * @author pax
 * @since 2020-03-19
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private static final String AUTH_HEADER = "TOKEN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取访问URL
        log.debug("IP={}", request.getRemoteAddr());
        log.debug("URL={}", request.getRequestURL().toString());
        String token = request.getHeader(AUTH_HEADER);
        if (StringUtils.isBlank(token)) {
            throw new UnifiedException(UnifiedCodeEnum.A0220, AUTH_HEADER);
        }

        // TODO: 向内部checkToken控制器方法，或外部统一认证服务，验证此token
        boolean ok = true;

        // 认证失败
        if (!ok) {
            throw new UnifiedException(UnifiedCodeEnum.A0220, token);
        }
        return true;
    }

    private void outputError(HttpServletResponse response, UnifiedCodeEnum unifiedCodeEnum) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSON.toJSONString(UnifiedResponseFactory.createFail(unifiedCodeEnum)));
    }
}

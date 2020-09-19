package io.github.xfally.springdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 统一日志AOP
 *
 * @author pax
 * @since 2020-03-19
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(* io.github.xfally.springdemo.controller.*.*(..))")
    public void webLog() {

    }

    @Before(value = "webLog()")
    public void before(JoinPoint point) {
        startTime.set(System.currentTimeMillis());

        log.debug("before...");
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("IP={}", request.getRemoteAddr());
        log.debug("URL={}", request.getRequestURL().toString());
        log.debug("HTTP_METHOD={}", request.getMethod());
        log.debug("CLASS_NAME={}.{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        log.debug("ARGS={}", Arrays.toString(point.getArgs()));
    }


    @AfterReturning(value = "webLog()", returning = "ret")
    public void afterReturning(Object ret) {
        log.debug("afterReturning...");
        log.debug("Resp={}", ret);
        log.debug("SpendTime={}(ms)", (System.currentTimeMillis() - startTime.get()));
        startTime.remove();
    }
}

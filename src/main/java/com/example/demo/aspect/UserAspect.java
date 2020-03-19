package com.example.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author pax
 */
@Component
/// NOTE: 暂不使用
//@Aspect
@Slf4j
public class UserAspect {
    @Pointcut("execution(* com.example.demo.service.impl.UserServiceImpl.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        log.debug("before...");
    }

    /// conflict with UnifiedResultHandlerAdvice
    @Around("pointCut()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("around start...");
        log.debug("around signature={}", proceedingJoinPoint.getSignature());
        log.debug("around args={}", proceedingJoinPoint.getArgs());
        Object result = proceedingJoinPoint.proceed();
        log.debug("around result={}", result);
        log.debug("around end...");
    }

    @After("pointCut()")
    public void after() {
        log.debug("after...");
    }

    @AfterReturning("pointCut()")
    public void afterReturning() {
        log.debug("afterReturning...");
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing() {
        log.debug("afterThrowing...");
    }
}

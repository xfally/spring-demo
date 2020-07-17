package com.example.demo.aspect;

import com.example.demo.config.DataSourceContextHolder;
import com.example.demo.config.DataSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据源AOP。根据DAO请求不同，使用不同的数据源
 *
 * @author pax
 */
@Component
@Order(value = -100)
@Slf4j
@Aspect
public class DataSourceSwitchAspect {

    @Pointcut("execution(* com.example.demo.dao.ds1.mapper..*.*(..))")
    private void ds1Aspect() {
    }

    @Pointcut("execution(* com.example.demo.dao.ds2.mapper..*.*(..))")
    private void ds2Aspect() {
    }

    @Before("ds1Aspect()")
    public void ds1() {
        log.debug("Switching dataSource to ds1...");
        DataSourceContextHolder.setDataSource(DataSourceEnum.DS1);
    }

    @Before("ds2Aspect()")
    public void ds2() {
        log.debug("Switching dataSource to ds2...");
        DataSourceContextHolder.setDataSource(DataSourceEnum.DS2);
    }
}

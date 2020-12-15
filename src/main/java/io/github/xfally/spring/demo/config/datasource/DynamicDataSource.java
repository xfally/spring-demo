package io.github.xfally.spring.demo.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义数据源动态路由
 *
 * @author pax
 * @since 2020-03-19
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceContextHolder.getDataSource();
        log.debug("Using dataSource: {}", dataSource);
        return dataSource;
    }
}

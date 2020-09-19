package io.github.xfally.springdemo.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class Ds1Config {

    /**
     * 获取数据库数据源对象
     *
     * @return 数据源对象
     */
    @Primary
    @Bean(name = "ds1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.ds1")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    /**
     * 该方法仅在需要使用JdbcTemplate对象时选用
     *
     * @param dataSource 注入 DataSource
     * @return 数据源JdbcTemplate对象
     */
    @Primary
    @Bean(name = "ds1JdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("ds1") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}

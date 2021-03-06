package io.github.xfally.spring.demo.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    // repository包名
    basePackages = Ds2JpaConfig.REPOSITORY_PACKAGE,
    // 实体管理bean名称
    entityManagerFactoryRef = "ds2EntityManagerFactory",
    // 事务管理bean名称
    transactionManagerRef = "ds2TransactionManager")
public class Ds2JpaConfig {
    static final String REPOSITORY_PACKAGE = "io.github.xfally.spring.demo.dao.ds2.repository";
    private static final String ENTITY_PACKAGE = "io.github.xfally.spring.demo.dao.ds2.entity";

    /**
     * 扫描spring.jpa开头的配置信息
     *
     * @return jpa配置信息
     */
    @Bean(name = "ds2JpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.ds2")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    /**
     * 获取主库实体管理工厂对象
     *
     * @param dataSource    注入 DataSource
     * @param jpaProperties 注入 JpaProperties
     * @param builder       注入 EntityManagerFactoryBuilder
     * @return 实体管理工厂对象
     */
    @Bean(name = "ds2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("ds2") DataSource dataSource, @Qualifier("ds2JpaProperties") JpaProperties jpaProperties, EntityManagerFactoryBuilder builder) {
        return builder
            // 设置数据源
            .dataSource(dataSource)
            // 设置jpa配置
            .properties(jpaProperties.getProperties())
            // .properties(properties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings()))
            // 设置实体包名
            .packages(ENTITY_PACKAGE)
            // 设置持久化单元名，用于@PersistenceContext注解获取EntityManager时指定数据源
            .persistenceUnit("ds2PersistenceUnit").build();
    }

    /**
     * 获取实体管理对象
     *
     * @param factory 注入 EntityManagerFactory
     * @return 实体管理对象
     */
    @Bean(name = "ds2EntityManager")
    public EntityManager entityManager(@Qualifier("ds2EntityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }

    /**
     * 获取主库事务管理对象
     *
     * @param factory 注入 EntityManagerFactory
     * @return 事务管理对象
     */
    @Bean(name = "ds2TransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("ds2EntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}

package io.github.xfally.springdemo.dao.ds1.repository;

import io.github.xfally.springdemo.dao.ds1.entity.CustomerDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository 接口
 *
 * @author pax
 * @since 2020-05-08
 **/
public interface CustomerRepository extends JpaRepository<CustomerDO, Long> {
}

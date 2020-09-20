package io.github.xfally.spring.demo.dao.ds1.repository;

import io.github.xfally.spring.demo.dao.ds1.entity.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository 接口
 *
 * @author pax
 * @since 2020-05-08
 **/
public interface OrderRepository extends JpaRepository<OrderDO, Long> {
}

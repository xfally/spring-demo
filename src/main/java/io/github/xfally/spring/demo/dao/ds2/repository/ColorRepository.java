package io.github.xfally.spring.demo.dao.ds2.repository;

import io.github.xfally.spring.demo.dao.ds2.entity.ColorDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Repository 接口
 *
 * @author pax
 * @since 2020-05-08
 **/
public interface ColorRepository extends JpaRepository<ColorDO, Long> {
    Set<ColorDO> findByName(String name);
}

package io.github.xfally.spring.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xfally.spring.demo.common.model.UnifiedCodeEnum;
import io.github.xfally.spring.demo.common.model.UnifiedPage;
import io.github.xfally.spring.demo.common.model.UnifiedQuery;
import io.github.xfally.spring.demo.common.response.UnifiedException;
import io.github.xfally.spring.demo.dao.ds1.entity.CustomerDO;
import io.github.xfally.spring.demo.dao.ds1.mapper.CustomerMapper;
import io.github.xfally.spring.demo.service.ICustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerDO> implements ICustomerService {

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'customer_' + #id")
    public CustomerDO getCustomer(@Valid @NotNull Long id) {
        CustomerDO customerDO = getById(id);
        if (customerDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return customerDO;
    }

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'customer_list'")
    public List<CustomerDO> listCustomers() {
        List<CustomerDO> customerDOList = list();
        if (customerDOList == null) {
            return new ArrayList<>();
        }
        return customerDOList;
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<CustomerDO> queryCustomers(UnifiedQuery unifiedQuery,
                                                  String name) {
        LambdaQueryWrapper<CustomerDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            if (unifiedQuery.getEqual()) {
                lambdaQueryWrapper.eq(CustomerDO::getName, name);
            } else {
                lambdaQueryWrapper.like(CustomerDO::getName, name);
            }
        }
        Page<CustomerDO> page = page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()), lambdaQueryWrapper);
        return UnifiedPage.ofMbp(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'customer_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'customer_' + #result.id", condition = "#result.id != 'null'")
    public CustomerDO saveCustomer(CustomerDO customerDO) {
        save(customerDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return customerDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'customer_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'customer_' + #result.id")
    public CustomerDO updateCustomer(CustomerDO customerDO) {
        if (getById(customerDO.getId()) == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, customerDO.getId());
        }
        updateById(customerDO);
        return customerDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'customer_' + #id", beforeInvocation = false),
            @CacheEvict(value = "spring-demo", key = "'customer_list'", beforeInvocation = false)
        }
    )
    public Boolean removeCustomer(Long id) {
        CustomerDO customerDO = getById(id);
        if (customerDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return removeById(id);
    }

}

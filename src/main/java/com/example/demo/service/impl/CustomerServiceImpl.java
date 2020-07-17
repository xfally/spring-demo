package com.example.demo.service.impl;

import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.dao.ds1.entity.CustomerDO;
import com.example.demo.dao.ds1.repository.CustomerRepository;
import com.example.demo.service.ICustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * 客户信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'customer_' + #id")
    public CustomerDO getCustomer(@Valid @NotNull Long id) {
        Optional<CustomerDO> optionalCustomerDO = customerRepository.findById(id);
        if (!optionalCustomerDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return optionalCustomerDO.get();
    }

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'customer_list'")
    public List<CustomerDO> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<CustomerDO> queryCustomers(UnifiedQuery unifiedQuery,
                                                  String name) {
        if (unifiedQuery.getCurrent() <= 0) {
            unifiedQuery.setCurrent(1);
        }
        Pageable pageable = PageRequest.of(unifiedQuery.getCurrent() - 1, unifiedQuery.getSize());
        Page<CustomerDO> page;
        if (!StringUtils.isBlank(name)) {
            CustomerDO customerDO = new CustomerDO();
            customerDO.setName(name);
            Example<CustomerDO> example = Example.of(customerDO);
            page = customerRepository.findAll(example, pageable);
        } else {
            page = customerRepository.findAll(pageable);
        }
        UnifiedPage<CustomerDO> unifiedPage = UnifiedPage.ofJpa(page);
        unifiedPage.setCurrent(unifiedPage.getCurrent() + 1);
        return unifiedPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'customer_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'customer_' + #result.id", condition = "#result.id != 'null'")
    public CustomerDO saveCustomer(CustomerDO customerDO) {
        customerDO.setId(null);
        customerDO = customerRepository.save(customerDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return customerDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'customer_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'customer_' + #result.id")
    public CustomerDO updateCustomer(CustomerDO customerDO) {
        if (!customerRepository.existsById(customerDO.getId())) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, customerDO.getId());
        }
        customerRepository.save(customerDO);
        return customerDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'customer_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'customer_list'", beforeInvocation = false)
        }
    )
    public Boolean removeCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        customerRepository.deleteById(id);
        return true;
    }

}

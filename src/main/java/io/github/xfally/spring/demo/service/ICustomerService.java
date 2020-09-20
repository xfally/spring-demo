package io.github.xfally.spring.demo.service;

import io.github.xfally.spring.demo.common.model.UnifiedPage;
import io.github.xfally.spring.demo.common.model.UnifiedQuery;
import io.github.xfally.spring.demo.dao.ds1.entity.CustomerDO;

import java.util.List;

/**
 * 客户信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface ICustomerService {
    CustomerDO getCustomer(Long id);

    List<CustomerDO> listCustomers();


    UnifiedPage<CustomerDO> queryCustomers(UnifiedQuery unifiedQuery,
                                           String name);

    CustomerDO saveCustomer(CustomerDO customerDO);

    CustomerDO updateCustomer(CustomerDO customerDO);

    Boolean removeCustomer(Long id);
}

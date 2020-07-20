package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.dao.ds1.entity.CustomerDO;

import java.util.List;

/**
 * 客户信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface ICustomerService extends IService<CustomerDO> {
    CustomerDO getCustomer(Long id);

    List<CustomerDO> listCustomers();


    UnifiedPage<CustomerDO> queryCustomers(UnifiedQuery unifiedQuery,
                                           String name);

    CustomerDO saveCustomer(CustomerDO customerDO);

    CustomerDO updateCustomer(CustomerDO customerDO);

    Boolean removeCustomer(Long id);
}

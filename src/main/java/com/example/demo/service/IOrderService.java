package com.example.demo.service;

import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.dao.ds1.entity.OrderDO;

import java.util.List;

/**
 * 订单信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface IOrderService {
    OrderDO getOrder(Long id);

    List<OrderDO> listOrders();


    UnifiedPage<OrderDO> queryOrders(UnifiedQuery unifiedQuery);

    OrderDO saveOrder(OrderDO orderDO);

    OrderDO updateOrder(OrderDO orderDO);

    Boolean removeOrder(Long id);
}

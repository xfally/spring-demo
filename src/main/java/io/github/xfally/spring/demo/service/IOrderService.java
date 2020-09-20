package io.github.xfally.spring.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xfally.spring.demo.common.model.UnifiedPage;
import io.github.xfally.spring.demo.common.model.UnifiedQuery;
import io.github.xfally.spring.demo.dao.ds1.entity.OrderDO;

import java.util.List;

/**
 * 订单信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface IOrderService extends IService<OrderDO> {
    OrderDO getOrder(Long id);

    List<OrderDO> listOrders();


    UnifiedPage<OrderDO> queryOrders(UnifiedQuery unifiedQuery);

    OrderDO saveOrder(OrderDO orderDO);

    OrderDO updateOrder(OrderDO orderDO);

    Boolean removeOrder(Long id);
}

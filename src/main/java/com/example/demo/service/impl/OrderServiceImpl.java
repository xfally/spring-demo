package com.example.demo.service.impl;

import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.dao.ds1.entity.OrderDO;
import com.example.demo.dao.ds1.repository.OrderRepository;
import com.example.demo.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
 * 订单信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Cacheable(value = "demo", condition = "#result != 'null'", key = "'order_' + #id")
    public OrderDO getOrder(@Valid @NotNull Long id) {
        Optional<OrderDO> optionalOrderDO = orderRepository.findById(id);
        if (!optionalOrderDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        return optionalOrderDO.get();
    }

    @Override
    @Cacheable(value = "demo", condition = "#result != 'null'", key = "'order_list'")
    public List<OrderDO> listOrders() {
        return orderRepository.findAll();
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<OrderDO> queryOrders(UnifiedQuery unifiedQuery) {
        if (unifiedQuery.getCurrent() <= 0) {
            unifiedQuery.setCurrent(1);
        }
        Pageable pageable = PageRequest.of(unifiedQuery.getCurrent() - 1, unifiedQuery.getSize());
        Page<OrderDO> page = orderRepository.findAll(pageable);
        UnifiedPage<OrderDO> unifiedPage = UnifiedPage.ofJpa(page);
        unifiedPage.setCurrent(unifiedPage.getCurrent() + 1);
        return unifiedPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demo", key = "'order_' + #result.id", condition = "#result.id != 'null'")
    public OrderDO saveOrder(OrderDO orderDO) {
        orderDO.setId(null);
        orderDO = orderRepository.save(orderDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return orderDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demo", key = "'order_' + #result.id")
    public OrderDO updateOrder(OrderDO orderDO) {
        Optional<OrderDO> optionalOrderDO = orderRepository.findById(orderDO.getId());
        if (!optionalOrderDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, orderDO.getId());
        }
        OrderDO orderDO1 = optionalOrderDO.get();
        if (orderDO.getCustomerId() == null) {
            orderDO.setCustomerId(orderDO1.getCustomerId());
        }
        if (orderDO.getProductId() == null) {
            orderDO.setProductId(orderDO1.getProductId());
        }
        orderRepository.save(orderDO);
        return orderDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demo", key = "'order_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    public Boolean removeOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        orderRepository.deleteById(id);
        return true;
    }

}

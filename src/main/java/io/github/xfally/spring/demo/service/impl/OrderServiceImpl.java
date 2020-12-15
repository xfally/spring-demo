package io.github.xfally.spring.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xfally.spring.demo.common.model.UnifiedCodeEnum;
import io.github.xfally.spring.demo.common.model.UnifiedPage;
import io.github.xfally.spring.demo.common.model.UnifiedQuery;
import io.github.xfally.spring.demo.common.response.UnifiedException;
import io.github.xfally.spring.demo.dao.ds1.entity.OrderDO;
import io.github.xfally.spring.demo.dao.ds1.mapper.OrderMapper;
import io.github.xfally.spring.demo.service.IOrderService;
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
 * 订单信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'order_' + #id")
    public OrderDO getOrder(@Valid @NotNull Long id) {
        OrderDO orderDO = getById(id);
        if (orderDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        return orderDO;
    }

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'order_list'")
    public List<OrderDO> listOrders() {
        List<OrderDO> orderDOList = list();
        if (orderDOList == null) {
            return new ArrayList<>();
        }
        return orderDOList;
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<OrderDO> queryOrders(UnifiedQuery unifiedQuery) {
        Page<OrderDO> page = page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()));
        return UnifiedPage.ofMbp(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'order_' + #result.id", condition = "#result.id != 'null'")
    public OrderDO saveOrder(OrderDO orderDO) {
        save(orderDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return orderDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'order_' + #result.id")
    public OrderDO updateOrder(OrderDO orderDO) {
        OrderDO orderDO1 = getById(orderDO.getId());
        if (orderDO1 == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, orderDO.getId());
        }
        if (orderDO.getCustomerId() == null) {
            orderDO.setCustomerId(orderDO1.getCustomerId());
        }
        if (orderDO.getProductId() == null) {
            orderDO.setProductId(orderDO1.getProductId());
        }
        updateById(orderDO);
        return orderDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'order_' + #id", beforeInvocation = false),
            @CacheEvict(value = "spring-demo", key = "'order_list'", beforeInvocation = false)
        }
    )
    public Boolean removeOrder(Long id) {
        OrderDO orderDO = getById(id);
        if (orderDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        return removeById(id);
    }

}

package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4AddAction;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.CustomerDO;
import com.example.demo.dao.ds1.entity.OrderDO;
import com.example.demo.dao.ds1.entity.ProductDO;
import com.example.demo.model.OrderInVO;
import com.example.demo.model.OrderOutVO;
import com.example.demo.service.ICustomerService;
import com.example.demo.service.IOrderService;
import com.example.demo.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单信息
 *
 * @author pax
 * @since 2020-03-19
 */
@Api(tags = {"订单信息"})
@UnifiedResponse
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProductService productService;

    @ApiOperation("获取订单信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_' + #id")
    public OrderOutVO getOrder(@ApiParam(value = "订单ID", required = true) @RequestParam @Valid @NotNull Long id) {
        OrderDO orderDO = orderService.getById(id);
        if (orderDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        CustomerDO customerDO = customerService.getById(orderDO.getCustomerId());
        ProductDO productDO = productService.getById(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("获取所有订单信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_list'")
    public List<OrderOutVO> listOrders() {
        List<OrderDO> orderDOList = orderService.list();
        if (orderDOList == null) {
            return new ArrayList<>();
        }
        return orderDOList
            .stream()
            .map(orderDO -> {
                CustomerDO customerDO = customerService.getById(orderDO.getCustomerId());
                ProductDO productDO = productService.getById(orderDO.getProductId());
                return OrderOutVO.of(orderDO, customerDO, productDO);
            })
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询订单信息")
    @PostMapping("query")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_page'")
    public UnifiedPage<OrderOutVO> queryOrders(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery) {
        Page<OrderDO> page = orderService.page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()));
        List<OrderOutVO> orderOutVOList = page.getRecords()
            .stream()
            .map(orderDO -> {
                CustomerDO customerDO = customerService.getById(orderDO.getCustomerId());
                ProductDO productDO = productService.getById(orderDO.getProductId());
                return OrderOutVO.of(orderDO, customerDO, productDO);
            })
            .collect(Collectors.toList());
        return UnifiedPage.of(page, orderOutVOList);
    }

    @ApiOperation("保存订单信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'order_' + #result.id", condition = "#result.id != 'null'")
    public OrderOutVO saveOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4AddAction.class) OrderInVO orderInVO) {
        OrderDO orderDO = OrderInVO.of(orderInVO);
        orderService.save(orderDO);
        // TEST: 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        CustomerDO customerDO = customerService.getById(orderDO.getCustomerId());
        ProductDO productDO = productService.getById(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("更新订单信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'order_' + #result.id")
    public OrderOutVO updateOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) OrderInVO orderInVO) {
        OrderDO orderDO = orderService.getById(orderInVO.getId());
        if (orderDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, orderInVO.getId());
        }
        if (orderInVO.getCustomerId() == null) {
            orderInVO.setCustomerId(orderDO.getCustomerId());
        }
        if (orderInVO.getProductId() == null) {
            orderInVO.setProductId(orderDO.getProductId());
        }
        orderDO = OrderInVO.of(orderInVO);
        orderService.updateById(orderDO);
        CustomerDO customerDO = customerService.getById(orderDO.getCustomerId());
        ProductDO productDO = productService.getById(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("删除订单信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'order_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'order_list'", beforeInvocation = false)
        }
    )
    public Boolean removeOrder(@ApiParam(value = "订单ID", required = true) @RequestParam @Valid @NotNull Long id) {
        OrderDO orderDO = orderService.getById(id);
        OrderOutVO orderOutVO = new OrderOutVO();
        if (orderDO == null) {
            orderOutVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        return orderService.removeById(id);
    }

}


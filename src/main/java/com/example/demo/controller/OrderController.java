package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4AddAction;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.Order;
import com.example.demo.model.OrderVO;
import com.example.demo.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
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
import java.util.LinkedList;
import java.util.List;

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

    @ApiOperation("获取订单信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_' + #id")
    public OrderVO getOrder(@ApiParam(value = "订单ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Order order = orderService.getById(id);
        OrderVO orderVO = new OrderVO();
        if (order == null) {
            orderVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }

    @ApiOperation("获取所有订单信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_list'")
    public List<OrderVO> listOrders() {
        List<Order> orders = orderService.list();
        if (orders == null) {
            return new ArrayList<>();
        }
        List<OrderVO> orderVOList = new LinkedList<>();
        for (Order order : orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @ApiOperation("分页获取所有订单信息")
    @GetMapping("page")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'order_page'")
    public Page<OrderVO> pageOrders(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                    @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size) {
        Page<Order> page = orderService.page(new Page<>(current, size));
        if (page == null) {
            return new Page<>();
        }
        List<OrderVO> orderVOList = new LinkedList<>();
        for (Order order : page.getRecords()) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            orderVOList.add(orderVO);
        }
        Page<OrderVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(orderVOList);
        return pageOut;
    }

    @ApiOperation("保存订单信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'order_' + #result.id", condition = "#result.id != 'null'")
    public OrderVO saveOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4AddAction.class) OrderVO orderVO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderVO, order);
        orderService.save(order);
        // TEST: 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        orderVO.setId(order.getId());
        return orderVO;
    }

    @ApiOperation("更新订单信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'order_' + #result.id")
    public OrderVO updateOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) OrderVO orderVO) {
        Order order = orderService.getById(orderVO);
        if (order == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1003, orderVO);
        }
        if (orderVO.getCustomerId() == null) {
            orderVO.setCustomerId(order.getCustomerId());
        }
        if (orderVO.getProductId() == null) {
            orderVO.setProductId(order.getProductId());
        }
        BeanUtils.copyProperties(orderVO, order);
        orderService.updateById(order);
        return orderVO;
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
        Order order = orderService.getById(id);
        OrderVO orderVO = new OrderVO();
        if (order == null) {
            orderVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1003, id);
        }
        return orderService.removeById(id);
    }

}


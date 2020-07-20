package com.example.demo.controller;


import com.example.demo.common.helper.Group4AddAction;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    public OrderOutVO getOrder(@ApiParam(value = "订单ID", required = true) @RequestParam @Valid @NotNull Long id) {
        OrderDO orderDO = orderService.getOrder(id);
        CustomerDO customerDO = customerService.getCustomer(orderDO.getCustomerId());
        ProductDO productDO = productService.getProduct(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("获取所有订单信息")
    @GetMapping("list")
    public List<OrderOutVO> listOrders() {
        List<OrderDO> orderDOList = orderService.listOrders();
        return orderDOList
            .stream()
            .map(orderDO -> {
                CustomerDO customerDO = customerService.getCustomer(orderDO.getCustomerId());
                ProductDO productDO = productService.getProduct(orderDO.getProductId());
                return OrderOutVO.of(orderDO, customerDO, productDO);
            })
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询订单信息")
    @PostMapping("query")
    public UnifiedPage<OrderOutVO> queryOrders(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery) {
        UnifiedPage<OrderDO> page = orderService.queryOrders(unifiedQuery);
        List<OrderOutVO> orderOutVOList = page.getRecords()
            .stream()
            .map(orderDO -> {
                CustomerDO customerDO = customerService.getCustomer(orderDO.getCustomerId());
                ProductDO productDO = productService.getProduct(orderDO.getProductId());
                return OrderOutVO.of(orderDO, customerDO, productDO);
            })
            .collect(Collectors.toList());
        return UnifiedPage.of(page, orderOutVOList);
    }

    @ApiOperation("保存订单信息")
    @PostMapping("save")
    public OrderOutVO saveOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4AddAction.class) OrderInVO orderInVO) {
        OrderDO orderDO = orderService.saveOrder(OrderInVO.of(orderInVO));
        CustomerDO customerDO = customerService.getCustomer(orderDO.getCustomerId());
        ProductDO productDO = productService.getProduct(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("更新订单信息")
    @PutMapping("update")
    public OrderOutVO updateOrder(@ApiParam(value = "订单信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) OrderInVO orderInVO) {
        OrderDO orderDO = orderService.updateOrder(OrderInVO.of(orderInVO));
        CustomerDO customerDO = customerService.getCustomer(orderDO.getCustomerId());
        ProductDO productDO = productService.getProduct(orderDO.getProductId());
        return OrderOutVO.of(orderDO, customerDO, productDO);
    }

    @ApiOperation("删除订单信息")
    @DeleteMapping("remove")
    public Boolean removeOrder(@ApiParam(value = "订单ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return orderService.removeOrder(id);
    }

}


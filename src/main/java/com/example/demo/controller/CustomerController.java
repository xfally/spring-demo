package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.Customer;
import com.example.demo.model.CustomerVO;
import com.example.demo.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
 * 客户信息
 * <p>
 * 前端控制器
 * </p>
 * 用`@Transactional`修饰类，则所有方法都启用事务。
 * 方法也可以同时加该注解，将优先采用方法的注解配置。
 * 如果觉得影响性能，可不给类注解，只给部分方法加事务注解。
 * <p>
 * 不用`@Controller`，而是用`@RestController`注解修饰类后，
 * 则所有的方法默认都返回JSON，XML或自定义mediaType内容到页面（即被`@ResponseBody`注解的效果），
 * 至于具体是哪种，MessageConverter接口的实现类会根据返回内容灵活判断。
 *
 * @author pax
 * @since 2020-03-19
 */
@Api(tags = {"客户信息"})
@UnifiedResponse
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @ApiOperation("获取客户信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'customer_' + #id")
    public CustomerVO getCustomer(@ApiParam(value = "客户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Customer customer = customerService.getById(id);
        if (customer == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return CustomerVO.of(customer);
    }

    @ApiOperation("获取所有客户信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'customer_list'")
    public List<CustomerVO> listCustomers() {
        List<Customer> customers = customerService.list();
        if (customers == null) {
            return new ArrayList<>();
        }
        return customers
            .stream()
            .map(CustomerVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页获取所有客户信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<CustomerVO> pageCustomers(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                          @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                          @ApiParam(value = "查询条件：客户名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            lambdaQueryWrapper.like(Customer::getName, name);
        }
        Page<Customer> page = customerService.page(new Page<>(current, size), lambdaQueryWrapper);
        return CustomerVO.of(page);
    }

    @ApiOperation("保存客户信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'customer_' + #result.id", condition = "#result.id != 'null'")
    public CustomerVO saveCustomer(@ApiParam(value = "客户信息", required = true) @RequestBody @Valid CustomerVO customerVO) {
        Customer customer = CustomerVO.of(customerVO);
        customerService.save(customer);
        // TEST: 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        customerVO.setId(customer.getId());
        return customerVO;
    }

    @ApiOperation("更新客户信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'customer_' + #result.id")
    public CustomerVO updateCustomer(@ApiParam(value = "客户信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) CustomerVO customerVO) {
        Customer customer = customerService.getById(customerVO.getId());
        if (customer == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, customerVO.getId());
        }
        customerService.updateById(CustomerVO.of(customerVO));
        return customerVO;
    }

    @ApiOperation("删除客户信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'customer_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'customer_list'", beforeInvocation = false)
        }
    )
    public Boolean removeCustomer(@ApiParam(value = "客户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Customer customer = customerService.getById(id);
        CustomerVO customerVO = new CustomerVO();
        if (customer == null) {
            customerVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return customerService.removeById(id);
    }

}


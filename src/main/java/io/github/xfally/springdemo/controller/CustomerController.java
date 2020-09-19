package io.github.xfally.springdemo.controller;


import io.github.xfally.springdemo.common.helper.Group4UpdateAction;
import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.common.response.UnifiedResponse;
import io.github.xfally.springdemo.dao.ds1.entity.CustomerDO;
import io.github.xfally.springdemo.model.CustomerInVO;
import io.github.xfally.springdemo.model.CustomerOutVO;
import io.github.xfally.springdemo.service.ICustomerService;
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
    public CustomerOutVO getCustomer(@ApiParam(value = "客户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return CustomerOutVO.of(customerService.getCustomer(id));
    }

    @ApiOperation("获取所有客户信息")
    @GetMapping("list")
    public List<CustomerOutVO> listCustomers() {
        return customerService.listCustomers()
            .stream()
            .map(CustomerOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询客户信息")
    @PostMapping("query")
    public UnifiedPage<CustomerOutVO> queryCustomers(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery,
                                                     @ApiParam(value = "查询条件：客户名") @RequestParam(required = false) @Valid String name) {
        UnifiedPage<CustomerDO> page = customerService.queryCustomers(unifiedQuery, name);
        List<CustomerOutVO> customerOutVOList = page.getRecords()
            .stream()
            .map(CustomerOutVO::of)
            .collect(Collectors.toList());
        return UnifiedPage.of(page, customerOutVOList);
    }

    @ApiOperation("保存客户信息")
    @PostMapping("save")
    public CustomerOutVO saveCustomer(@ApiParam(value = "客户信息", required = true) @RequestBody @Valid CustomerInVO customerInVO) {
        return CustomerOutVO.of(customerService.saveCustomer(CustomerInVO.of(customerInVO)));
    }

    @ApiOperation("更新客户信息")
    @PutMapping("update")
    public CustomerOutVO updateCustomer(@ApiParam(value = "客户信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) CustomerInVO customerInVO) {
        return CustomerOutVO.of(customerService.updateCustomer(CustomerInVO.of(customerInVO)));
    }

    @ApiOperation("删除客户信息")
    @DeleteMapping("remove")
    public Boolean removeCustomer(@ApiParam(value = "客户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return customerService.removeCustomer(id);
    }

}


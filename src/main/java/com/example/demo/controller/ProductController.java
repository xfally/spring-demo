package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.ProductDO;
import com.example.demo.model.ProductInVO;
import com.example.demo.model.ProductOutVO;
import com.example.demo.service.IProductService;
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
 * 产品信息
 *
 * @author pax
 * @since 2020-03-19
 */
@Api(tags = {"产品信息"})
@UnifiedResponse
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @ApiOperation("获取产品信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'product_' + #id")
    public ProductOutVO getProduct(@ApiParam(value = "产品ID", required = true) @RequestParam @Valid @NotNull Long id) {
        ProductDO productDO = productService.getById(id);
        if (productDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return ProductOutVO.of(productDO);
    }

    @ApiOperation("获取所有产品信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'product_list'")
    public List<ProductOutVO> listProducts() {
        List<ProductDO> productDOList = productService.list();
        if (productDOList == null) {
            return new ArrayList<>();
        }
        return productDOList
            .stream()
            .map(ProductOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询产品信息")
    @PostMapping("query")
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<ProductOutVO> queryProducts(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery,
                                                   @ApiParam(value = "查询条件：产品名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<ProductDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            if (unifiedQuery.getEqual()) {
                lambdaQueryWrapper.eq(ProductDO::getName, name);
            } else {
                lambdaQueryWrapper.like(ProductDO::getName, name);
            }
        }
        Page<ProductDO> page = productService.page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()), lambdaQueryWrapper);
        List<ProductOutVO> productOutVOList = page.getRecords()
            .stream()
            .map(ProductOutVO::of)
            .collect(Collectors.toList());
        return UnifiedPage.of(page, productOutVOList);
    }

    @ApiOperation("保存产品信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'product_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'product_' + #result.id", condition = "#result.id != 'null'")
    public ProductOutVO saveProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Valid ProductInVO productInVO) {
        ProductDO productDO = ProductInVO.of(productInVO);
        productService.save(productDO);
        // TEST: 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return ProductOutVO.of(productDO);
    }

    @ApiOperation("更新产品信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'product_' + #result.id")
    public ProductOutVO updateProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ProductInVO productInVO) {
        ProductDO productDO = productService.getById(productInVO.getId());
        if (productDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, productInVO.getId());
        }
        productDO = ProductInVO.of(productInVO);
        productService.updateById(productDO);
        return ProductOutVO.of(productDO);
    }

    @ApiOperation("删除产品信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'product_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'product_list'", beforeInvocation = false)
        }
    )
    public Boolean removeProduct(@ApiParam(value = "产品ID", required = true) @RequestParam @Valid @NotNull Long id) {
        ProductDO productDO = productService.getById(id);
        ProductOutVO productOutVO = new ProductOutVO();
        if (productDO == null) {
            productOutVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return productService.removeById(id);
    }

}


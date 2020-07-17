package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.Product;
import com.example.demo.model.ProductVO;
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
    public ProductVO getProduct(@ApiParam(value = "产品ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return ProductVO.of(product);
    }

    @ApiOperation("获取所有产品信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'product_list'")
    public List<ProductVO> listProducts() {
        List<Product> products = productService.list();
        if (products == null) {
            return new ArrayList<>();
        }
        return products
            .stream()
            .map(ProductVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页获取所有产品信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<ProductVO> pageProducts(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                        @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                        @ApiParam(value = "查询条件：产品名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<Product> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            lambdaQueryWrapper.like(Product::getName, name);
        }
        Page<Product> page = productService.page(new Page<>(current, size), lambdaQueryWrapper);
        return ProductVO.of(page);
    }

    @ApiOperation("保存产品信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'product_' + #result.id", condition = "#result.id != 'null'")
    public ProductVO saveProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Valid ProductVO productVO) {
        Product product = ProductVO.of(productVO);
        productService.save(product);
        // TEST: 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        productVO.setId(product.getId());
        return productVO;
    }

    @ApiOperation("更新产品信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'product_' + #result.id")
    public ProductVO updateProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ProductVO productVO) {
        Product product = productService.getById(productVO.getId());
        if (product == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, productVO.getId());
        }
        productService.updateById(ProductVO.of(productVO));
        return productVO;
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
        Product product = productService.getById(id);
        ProductVO productVO = new ProductVO();
        if (product == null) {
            productVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return productService.removeById(id);
    }

}


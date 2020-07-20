package com.example.demo.controller;


import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds1.entity.ProductDO;
import com.example.demo.model.ProductInVO;
import com.example.demo.model.ProductOutVO;
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
    public ProductOutVO getProduct(@ApiParam(value = "产品ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return ProductOutVO.of(productService.getProduct(id));
    }

    @ApiOperation("获取所有产品信息")
    @GetMapping("list")
    public List<ProductOutVO> listProducts() {
        return productService.listProducts()
            .stream()
            .map(ProductOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询产品信息")
    @PostMapping("query")
    public UnifiedPage<ProductOutVO> queryProducts(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery,
                                                   @ApiParam(value = "查询条件：产品名") @RequestParam(required = false) @Valid String name) {
        UnifiedPage<ProductDO> page = productService.queryProducts(unifiedQuery, name);
        List<ProductOutVO> productOutVOList = page.getRecords()
            .stream()
            .map(ProductOutVO::of)
            .collect(Collectors.toList());
        return UnifiedPage.of(page, productOutVOList);
    }

    @ApiOperation("保存产品信息")
    @PostMapping("save")
    public ProductOutVO saveProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Valid ProductInVO productInVO) {
        return ProductOutVO.of(productService.saveProduct(ProductInVO.of(productInVO)));
    }

    @ApiOperation("更新产品信息")
    @PutMapping("update")
    public ProductOutVO updateProduct(@ApiParam(value = "产品信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ProductInVO productInVO) {
        return ProductOutVO.of(productService.updateProduct(ProductInVO.of(productInVO)));
    }

    @ApiOperation("删除产品信息")
    @DeleteMapping("remove")
    public Boolean removeProduct(@ApiParam(value = "产品ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return productService.removeProduct(id);
    }

}


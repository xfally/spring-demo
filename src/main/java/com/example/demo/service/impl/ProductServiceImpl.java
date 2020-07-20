package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.dao.ds1.entity.ProductDO;
import com.example.demo.dao.ds1.mapper.ProductMapper;
import com.example.demo.service.IProductService;
import org.apache.commons.lang3.StringUtils;
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
 * 产品信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements IProductService {

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'product_' + #id")
    public ProductDO getProduct(@Valid @NotNull Long id) {
        ProductDO productDO = getById(id);
        if (productDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return productDO;
    }

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'product_list'")
    public List<ProductDO> listProducts() {
        List<ProductDO> productDOList = list();
        if (productDOList == null) {
            return new ArrayList<>();
        }
        return productDOList;
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<ProductDO> queryProducts(UnifiedQuery unifiedQuery,
                                                String name) {
        LambdaQueryWrapper<ProductDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            if (unifiedQuery.getEqual()) {
                lambdaQueryWrapper.eq(ProductDO::getName, name);
            } else {
                lambdaQueryWrapper.like(ProductDO::getName, name);
            }
        }
        Page<ProductDO> page = page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()), lambdaQueryWrapper);
        return UnifiedPage.of(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'product_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'product_' + #result.id", condition = "#result.id != 'null'")
    public ProductDO saveProduct(ProductDO productDO) {
        save(productDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return productDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'product_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'product_' + #result.id")
    public ProductDO updateProduct(ProductDO productDO) {
        if (getById(productDO.getId()) == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, productDO.getId());
        }
        updateById(productDO);
        return productDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'product_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'product_list'", beforeInvocation = false)
        }
    )
    public Boolean removeProduct(Long id) {
        ProductDO productDO = getById(id);
        if (productDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return removeById(id);
    }

}

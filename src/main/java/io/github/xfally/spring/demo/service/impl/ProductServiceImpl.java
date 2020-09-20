package io.github.xfally.spring.demo.service.impl;

import io.github.xfally.spring.demo.common.model.UnifiedCodeEnum;
import io.github.xfally.spring.demo.common.model.UnifiedPage;
import io.github.xfally.spring.demo.common.model.UnifiedQuery;
import io.github.xfally.spring.demo.common.response.UnifiedException;
import io.github.xfally.spring.demo.dao.ds1.entity.ProductDO;
import io.github.xfally.spring.demo.dao.ds1.repository.ProductRepository;
import io.github.xfally.spring.demo.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * 产品信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'product_' + #id")
    public ProductDO getProduct(@Valid @NotNull Long id) {
        Optional<ProductDO> optionalProductDO = productRepository.findById(id);
        if (!optionalProductDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return optionalProductDO.get();
    }

    @Override
    @Cacheable(value = "spring-demo", condition = "#result != 'null'", key = "'product_list'")
    public List<ProductDO> listProducts() {
        return productRepository.findAll();
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<ProductDO> queryProducts(UnifiedQuery unifiedQuery,
                                                String name) {
        if (unifiedQuery.getCurrent() <= 0) {
            unifiedQuery.setCurrent(1);
        }
        Pageable pageable = PageRequest.of(unifiedQuery.getCurrent() - 1, unifiedQuery.getSize());
        Page<ProductDO> page;
        if (!StringUtils.isBlank(name)) {
            ProductDO productDO = new ProductDO();
            productDO.setName(name);
            Example<ProductDO> example = Example.of(productDO);
            page = productRepository.findAll(example, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }
        UnifiedPage<ProductDO> unifiedPage = UnifiedPage.ofJpa(page);
        unifiedPage.setCurrent(unifiedPage.getCurrent() + 1);
        return unifiedPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'product_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'product_' + #result.id", condition = "#result.id != 'null'")
    public ProductDO saveProduct(ProductDO productDO) {
        productDO.setId(null);
        productDO = productRepository.save(productDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return productDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'product_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "spring-demo", key = "'product_' + #result.id")
    public ProductDO updateProduct(ProductDO productDO) {
        if (!productRepository.existsById(productDO.getId())) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, productDO.getId());
        }
        productRepository.save(productDO);
        return productDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "spring-demo", key = "'product_' + #id", beforeInvocation = false),
            @CacheEvict(value = "spring-demo", key = "'product_list'", beforeInvocation = false)
        }
    )
    public Boolean removeProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        productRepository.deleteById(id);
        return true;
    }

}

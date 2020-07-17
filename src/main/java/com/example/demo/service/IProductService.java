package com.example.demo.service;

import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.dao.ds1.entity.ProductDO;

import java.util.List;

/**
 * 产品信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface IProductService {
    ProductDO getProduct(Long id);

    List<ProductDO> listProducts();


    UnifiedPage<ProductDO> queryProducts(UnifiedQuery unifiedQuery,
                                         String name);

    ProductDO saveProduct(ProductDO productDO);

    ProductDO updateProduct(ProductDO productDO);

    Boolean removeProduct(Long id);
}

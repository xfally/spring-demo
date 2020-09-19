package io.github.xfally.springdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.dao.ds1.entity.ProductDO;

import java.util.List;

/**
 * 产品信息服务类
 *
 * @author pax
 * @since 2020-03-19
 */
public interface IProductService extends IService<ProductDO> {
    ProductDO getProduct(Long id);

    List<ProductDO> listProducts();


    UnifiedPage<ProductDO> queryProducts(UnifiedQuery unifiedQuery,
                                         String name);

    ProductDO saveProduct(ProductDO productDO);

    ProductDO updateProduct(ProductDO productDO);

    Boolean removeProduct(Long id);
}

package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ds1.entity.Product;
import com.example.demo.dao.ds1.mapper.ProductMapper;
import com.example.demo.service.IProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pax
 * @since 2020-03-19
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    //some custom method...
}

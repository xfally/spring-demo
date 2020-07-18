package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ds1.entity.OrderDO;
import com.example.demo.dao.ds1.mapper.OrderMapper;
import com.example.demo.service.IOrderService;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {
    //some custom method...
}

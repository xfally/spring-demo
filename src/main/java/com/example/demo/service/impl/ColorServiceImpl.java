package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ds2.entity.Color;
import com.example.demo.dao.ds2.mapper.ColorMapper;
import com.example.demo.service.IColorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class ColorServiceImpl extends ServiceImpl<ColorMapper, Color> implements IColorService {
    //some custom method...
}

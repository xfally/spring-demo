package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.model.UnifiedCodeEnum;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.dao.ds2.entity.ColorDO;
import com.example.demo.dao.ds2.mapper.ColorMapper;
import com.example.demo.service.IColorService;
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
 * 颜色信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class ColorServiceImpl extends ServiceImpl<ColorMapper, ColorDO> implements IColorService {

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'color_' + #id")
    public ColorDO getColor(@Valid @NotNull Long id) {
        ColorDO colorDO = getById(id);
        if (colorDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return colorDO;
    }

    @Override
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'color_list'")
    public List<ColorDO> listColors() {
        List<ColorDO> colorDOList = list();
        if (colorDOList == null) {
            return new ArrayList<>();
        }
        return colorDOList;
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<ColorDO> queryColors(UnifiedQuery unifiedQuery,
                                            String name) {
        LambdaQueryWrapper<ColorDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            if (unifiedQuery.getEqual()) {
                lambdaQueryWrapper.eq(ColorDO::getName, name);
            } else {
                lambdaQueryWrapper.like(ColorDO::getName, name);
            }
        }
        Page<ColorDO> page = page(new Page<>(unifiedQuery.getCurrent(), unifiedQuery.getSize()), lambdaQueryWrapper);
        return UnifiedPage.of(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'color_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "demoCache", key = "'color_' + #result.id", condition = "#result.id != 'null'")
    public ColorDO saveColor(ColorDO colorDO) {
        save(colorDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return colorDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'color_' + #result.id")
    public ColorDO updateColor(ColorDO colorDO) {
        if (getById(colorDO.getId()) == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, colorDO.getId());
        }
        updateById(colorDO);
        return colorDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'color_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'color_list'", beforeInvocation = false)
        }
    )
    public Boolean removeColor(Long id) {
        ColorDO colorDO = getById(id);
        if (colorDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return removeById(id);
    }

}

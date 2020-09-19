package io.github.xfally.springdemo.service.impl;

import io.github.xfally.springdemo.common.model.UnifiedCodeEnum;
import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.common.response.UnifiedException;
import io.github.xfally.springdemo.dao.ds2.entity.ColorDO;
import io.github.xfally.springdemo.dao.ds2.repository.ColorRepository;
import io.github.xfally.springdemo.service.IColorService;
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
 * 颜色信息服务实现类
 *
 * @author pax
 * @since 2020-05-08
 */
@Service
public class ColorServiceImpl implements IColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    @Cacheable(value = "springdemo", condition = "#result != 'null'", key = "'color_' + #id")
    public ColorDO getColor(@Valid @NotNull Long id) {
        Optional<ColorDO> optionalColorDO = colorRepository.findById(id);
        if (!optionalColorDO.isPresent()) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return optionalColorDO.get();
    }

    @Override
    @Cacheable(value = "springdemo", condition = "#result != 'null'", key = "'color_list'")
    public List<ColorDO> listColors() {
        return colorRepository.findAll();
    }

    @Override
    // 因为有搜索条件，命中率低，不采用缓存
    public UnifiedPage<ColorDO> queryColors(UnifiedQuery unifiedQuery,
                                            String name) {
        if (unifiedQuery.getCurrent() <= 0) {
            unifiedQuery.setCurrent(1);
        }
        Pageable pageable = PageRequest.of(unifiedQuery.getCurrent() - 1, unifiedQuery.getSize());
        Page<ColorDO> page;
        if (!StringUtils.isBlank(name)) {
            ColorDO colorDO = new ColorDO();
            colorDO.setName(name);
            Example<ColorDO> example = Example.of(colorDO);
            page = colorRepository.findAll(example, pageable);
        } else {
            page = colorRepository.findAll(pageable);
        }
        UnifiedPage<ColorDO> unifiedPage = UnifiedPage.ofJpa(page);
        unifiedPage.setCurrent(unifiedPage.getCurrent() + 1);
        return unifiedPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "springdemo", key = "'color_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "springdemo", key = "'color_' + #result.id", condition = "#result.id != 'null'")
    public ColorDO saveColor(ColorDO colorDO) {
        colorDO.setId(null);
        colorDO = colorRepository.save(colorDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return colorDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "springdemo", key = "'color_list'", beforeInvocation = false)
        }
    )
    @CachePut(value = "springdemo", key = "'color_' + #result.id")
    public ColorDO updateColor(ColorDO colorDO) {
        if (!colorRepository.existsById(colorDO.getId())) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, colorDO.getId());
        }
        colorRepository.save(colorDO);
        return colorDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "springdemo", key = "'color_' + #id", beforeInvocation = false),
            @CacheEvict(value = "springdemo", key = "'color_list'", beforeInvocation = false)
        }
    )
    public Boolean removeColor(Long id) {
        if (!colorRepository.existsById(id)) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        colorRepository.deleteById(id);
        return true;
    }

}

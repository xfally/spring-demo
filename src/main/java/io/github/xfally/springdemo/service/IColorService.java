package io.github.xfally.springdemo.service;

import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.dao.ds2.entity.ColorDO;

import java.util.List;

/**
 * 颜色信息服务
 *
 * @author pax
 * @since 2020-05-08
 */
public interface IColorService {
    ColorDO getColor(Long id);

    List<ColorDO> listColors();

    UnifiedPage<ColorDO> queryColors(UnifiedQuery unifiedQuery,
                                     String name);

    ColorDO saveColor(ColorDO colorDO);

    ColorDO updateColor(ColorDO colorDO);

    Boolean removeColor(Long id);
}
package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.model.UnifiedPage;
import com.example.demo.common.model.UnifiedQuery;
import com.example.demo.dao.ds2.entity.ColorDO;

import java.util.List;

/**
 * 颜色信息服务
 *
 * @author pax
 * @since 2020-05-08
 */
public interface IColorService extends IService<ColorDO> {
    ColorDO getColor(Long id);

    List<ColorDO> listColors();

    UnifiedPage<ColorDO> queryColors(UnifiedQuery unifiedQuery,
                                     String name);

    ColorDO saveColor(ColorDO colorDO);

    ColorDO updateColor(ColorDO colorDO);

    Boolean removeColor(Long id);
}

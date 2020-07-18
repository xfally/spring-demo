package com.example.demo.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.dao.ds2.entity.ColorDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 颜色VO
 *
 * @author pax
 */
@ApiModel("颜色VO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ColorVO implements Serializable {
    @ApiModelProperty("颜色ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("颜色名")
    @NotBlank(message = "颜色名不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static Page<ColorVO> of(Page<ColorDO> page) {
        if (page == null) {
            return new Page<>();
        }
        List<ColorVO> colorVOList = page.getRecords()
            .stream()
            .map(ColorVO::of)
            .collect(Collectors.toList());
        Page<ColorVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(colorVOList);
        return pageOut;
    }

    public static ColorVO of(ColorDO colorDO) {
        if (colorDO == null) {
            return null;
        }
        ColorVO colorVO = new ColorVO();
        BeanUtils.copyProperties(colorDO, colorVO);
        return colorVO;
    }

    public static ColorDO of(ColorVO colorVO) {
        if (colorVO == null) {
            return null;
        }
        ColorDO colorDO = new ColorDO();
        BeanUtils.copyProperties(colorVO, colorDO);
        return colorDO;
    }
}

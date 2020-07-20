package com.example.demo.model;

import com.example.demo.dao.ds2.entity.ColorDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 颜色OutVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("颜色OutVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ColorOutVO implements Serializable {
    @ApiModelProperty("颜色ID")
    private Long id;

    @ApiModelProperty("颜色名")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static ColorOutVO of(ColorDO colorDO) {
        if (colorDO == null) {
            return null;
        }
        ColorOutVO colorOutVO = new ColorOutVO();
        BeanUtils.copyProperties(colorDO, colorOutVO);
        return colorOutVO;
    }
}

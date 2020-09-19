package io.github.xfally.springdemo.model;

import io.github.xfally.springdemo.common.helper.Group4UpdateAction;
import io.github.xfally.springdemo.dao.ds2.entity.ColorDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 颜色InVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("颜色InVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ColorInVO implements Serializable {
    @ApiModelProperty("颜色ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("颜色名")
    @NotBlank(message = "颜色名不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static ColorDO of(ColorInVO colorInVO) {
        if (colorInVO == null) {
            return null;
        }
        ColorDO colorDO = new ColorDO();
        BeanUtils.copyProperties(colorInVO, colorDO);
        return colorDO;
    }
}

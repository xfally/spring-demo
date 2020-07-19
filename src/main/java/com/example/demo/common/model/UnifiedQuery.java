package com.example.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 统一查询条件
 *
 * @author pax
 */
@ApiModel("统一查询条件")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnifiedQuery implements Serializable {

    @ApiModelProperty(value = "精确匹配或模糊匹配，默认false模糊匹配")
    private Boolean equal = false;

    @ApiModelProperty(value = "当前页码，至少为0，默认值为0")
    @Min(value = 0, message = "页数必须大于等于0")
    private Integer current = 0;

    @ApiModelProperty(value = "每页数量，至少为1，默认值为10")
    @Min(value = 1, message = "每页数量必须大于等于1")
    private Integer size = 10;
}

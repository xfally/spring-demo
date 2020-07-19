package com.example.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一返回DTO
 *
 * @author pax
 */
@ApiModel("统一返回DTO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnifiedResponseDTO<T> implements Serializable {

    @ApiModelProperty(value = "结果，true成功，false失败")
    private Boolean result;

    @ApiModelProperty(value = "错误码")
    private String code;

    @ApiModelProperty(value = "消息")
    private String msg;

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;
}

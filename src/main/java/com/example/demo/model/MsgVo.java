package com.example.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * Msg info
 *
 * @author pax
 */
@ApiModel("消息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MsgVo {
    @ApiModelProperty("消息内容")
    @NotNull(message = "消息内容不可为null")
    private String content;

}

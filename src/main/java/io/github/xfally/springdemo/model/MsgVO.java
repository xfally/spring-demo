package io.github.xfally.springdemo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 消息VO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("消息VO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MsgVO {
    @ApiModelProperty("消息内容")
    @NotNull(message = "消息内容不可为null")
    private String content;

}

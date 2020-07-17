package com.example.demo.model;

import com.example.demo.common.helper.Group4AddAction;
import com.example.demo.common.helper.Group4UpdateAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单VO
 *
 * @author pax
 */
@ApiModel("订单VO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderVO implements Serializable {
    @ApiModelProperty("订单ID")
    @NotNull(message = "执行更新操作时，ID不可为空", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("客户ID")
    @NotNull(message = "执行新增操作时，客户ID不可为空", groups = Group4AddAction.class)
    private Long customerId;

    @ApiModelProperty("产品ID")
    @NotNull(message = "执行新增操作时，产品ID不可为空", groups = Group4AddAction.class)
    private Long productId;

    @ApiModelProperty("购买数量")
    @NotNull(message = "购买数量不能为NULL")
    @Min(value = 1, message = "购买数量需大于或等于1")
    @Value("#{1}")
    private Integer quantity;

    @ApiModelProperty("描述")
    private String note;
}

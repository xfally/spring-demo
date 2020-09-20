package io.github.xfally.spring.demo.dao.ds1.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单DO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel(value = "订单DO", description = "")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "t_order")
public class OrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "客户ID")
    @Column(name = "customer_id")
    private Long customerId;

    @ApiModelProperty(value = "产品ID")
    @Column(name = "product_id")
    private Long productId;

    @ApiModelProperty(value = "购买数量")
    @Column(name = "quantity")
    private Integer quantity;

    @ApiModelProperty(value = "描述")
    @Column(name = "note")
    private String note;

}

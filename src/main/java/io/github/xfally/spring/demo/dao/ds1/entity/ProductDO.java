package io.github.xfally.spring.demo.dao.ds1.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品DO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel(value = "产品DO", description = "")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "t_product")
public class ProductDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "产品名")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "描述")
    @Column(name = "note")
    private String note;

}

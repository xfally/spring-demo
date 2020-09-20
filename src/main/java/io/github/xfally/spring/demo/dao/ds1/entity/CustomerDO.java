package io.github.xfally.spring.demo.dao.ds1.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 客户DO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel(value = "客户DO", description = "")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "t_customer")
public class CustomerDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "客户名")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "客户年龄")
    @Column(name = "age")
    private Integer age;

    @ApiModelProperty(value = "描述")
    @Column(name = "note")
    private String note;

}

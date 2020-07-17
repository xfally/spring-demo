package com.example.demo.dao.ds2.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 颜色DO
 *
 * @author pax
 * @since 2020-05-08
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "t_color")
@ApiModel(value = "颜色DO", description = "")
public class ColorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "颜色ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "颜色名")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "描述")
    @Column(name = "note")
    private String note;

}

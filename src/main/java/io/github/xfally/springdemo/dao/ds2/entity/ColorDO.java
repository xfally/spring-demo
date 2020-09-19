package io.github.xfally.springdemo.dao.ds2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 颜色DO
 *
 * @author pax
 * @since 2020-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_color")
@ApiModel(value = "颜色DO", description = "")
public class ColorDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "颜色ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "颜色名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField("note")
    private String note;

}

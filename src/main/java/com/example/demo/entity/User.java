package com.example.demo.entity;

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
 * <p>
 * User对象
 * </p>
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel(value = "User对象", description = "")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 一般来说，数据库的id字段设置了`AUTO_INCREMENT`（当不指定），那么此处自动生成的注解是`IdType.AUTO`。
     * Java空间User实体的id为`null`或任意数值（将被忽略），存入数据库时都会采用自动增长的id，比如`1，2，3，4...`等。
     * <p>
     * 如果想生成分布式全局唯一id，可以采用“雪花算法”，此处设置`IdType.ASSIGN_ID`即可实现。
     * Java空间的User实体的id必须为`null`，那么存入数据库中的id就如`1242739154052345858`。
     * 如果User实体的id为其他具体数值，将直接使用该值，“雪花算法”失效。
     * 更多参考：https://mp.baomidou.com/guide/annotation.html#tableid
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "描述")
    @TableField("note")
    private String note;

}

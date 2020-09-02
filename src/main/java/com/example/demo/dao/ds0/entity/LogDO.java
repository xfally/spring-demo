package com.example.demo.dao.ds0.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志DO
 *
 * @author pax
 * @since 2020-08-31
 */
@ApiModel(value = "日志DO", description = "")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document("t_log")
public class LogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    // mongodb不支持Long型主键，推荐用String或BigInteger
    // 参考 https://docs.spring.io/spring-data/data-mongo/docs/1.7.0.M1/reference/html/#mongo-template.id-handling
    @ApiModelProperty(value = "日志ID")
    @Id
    private String id;

    @ApiModelProperty(value = "日志名")
    @Field("name")
    private String name;

    @ApiModelProperty(value = "描述")
    @Field("note")
    private String note;

    @ApiModelProperty(value = "日志等级")
    @Field("level")
    private String level;

    @ApiModelProperty(value = "发生时间")
    @Field("date")
    private Date date;
}

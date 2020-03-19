package com.example.demo.model;

import com.example.demo.common.helper.GroupsUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User info
 *
 * @author pax
 */
@ApiModel("用户信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserVO implements Serializable {
    @ApiModelProperty("用户ID")
    @NotNull(message = "执行更新操作时，ID不可为空", groups = GroupsUpdate.class)
    private Long id;

    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不可为null")
    private String name;

    @ApiModelProperty("用户年龄")
    @NotNull(message = "用户年龄不可为空")
    //@Min(value = 0, message = "年龄需大于等于0")
    //@Max(value = 200, message = "年龄需小于等于200")
    @Range(min = 0, max = 200, message = "年龄需在0~200岁之间")
    @Value("#{0}")
    private Integer age;

    @ApiModelProperty("用户描述")
    private String note;
}

package com.example.demo.model;

import com.example.demo.common.helper.GroupsUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Group info
 *
 * @author pax
 */
@ApiModel("组信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GroupVO implements Serializable {
    @ApiModelProperty("组ID")
    @NotNull(message = "执行更新操作时，ID不可为空", groups = GroupsUpdate.class)
    private Long id;

    @ApiModelProperty("组名")
    @NotNull(message = "组名不可为null")
    private String name;

    @ApiModelProperty("组描述")
    private String note;
}

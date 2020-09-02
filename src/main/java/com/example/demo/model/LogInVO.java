package com.example.demo.model;

import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.dao.ds0.entity.LogDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志InVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("日志InVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogInVO implements Serializable {
    @ApiModelProperty("日志ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private String id;

    @ApiModelProperty("日志名")
    @NotBlank(message = "日志名不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    @ApiModelProperty("日志等级")
    private String level;

    @ApiModelProperty("发生时间")
    private Date date;

    public static LogDO of(LogInVO logInVO) {
        if (logInVO == null) {
            return null;
        }
        LogDO logDO = new LogDO();
        BeanUtils.copyProperties(logInVO, logDO);
        return logDO;
    }
}

package io.github.xfally.spring.demo.model;

import io.github.xfally.spring.demo.dao.ds0.entity.LogDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志OutVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("日志OutVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogOutVO implements Serializable {
    @ApiModelProperty("日志ID")
    private String id;

    @ApiModelProperty("日志名")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    @ApiModelProperty("日志等级")
    private String level;

    @ApiModelProperty("发生时间")
    private Date date;

    public static LogOutVO of(LogDO logDO) {
        if (logDO == null) {
            return null;
        }
        LogOutVO logOutVO = new LogOutVO();
        BeanUtils.copyProperties(logDO, logOutVO);
        return logOutVO;
    }
}

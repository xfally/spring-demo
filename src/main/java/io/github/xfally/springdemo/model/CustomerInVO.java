package io.github.xfally.springdemo.model;

import io.github.xfally.springdemo.common.helper.Group4UpdateAction;
import io.github.xfally.springdemo.dao.ds1.entity.CustomerDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客户InVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("客户InVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerInVO implements Serializable {
    @ApiModelProperty("客户ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("客户名")
    @NotBlank(message = "客户名不可为空")
    private String name;

    @ApiModelProperty("客户年龄")
    @NotNull(message = "客户年龄不可为NULL")
    //@Min(value = 0, message = "年龄需大于等于0")
    //@Max(value = 200, message = "年龄需小于等于200")
    @Range(min = 0, max = 200, message = "年龄需在0~200岁之间")
    private Integer age;

    @ApiModelProperty("描述")
    private String note;

    public static CustomerDO of(CustomerInVO customerInVO) {
        if (customerInVO == null) {
            return null;
        }
        CustomerDO customerDO = new CustomerDO();
        BeanUtils.copyProperties(customerInVO, customerDO);
        return customerDO;
    }
}

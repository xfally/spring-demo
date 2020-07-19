package com.example.demo.model;

import com.example.demo.dao.ds1.entity.CustomerDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 客户OutVO
 *
 * @author pax
 */
@ApiModel("客户OutVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerOutVO implements Serializable {
    @ApiModelProperty("客户ID")
    private Long id;

    @ApiModelProperty("客户名")
    private String name;

    @ApiModelProperty("客户年龄")
    private Integer age;

    @ApiModelProperty("描述")
    private String note;

    public static CustomerOutVO of(CustomerDO customerDO) {
        if (customerDO == null) {
            return null;
        }
        CustomerOutVO customerOutVO = new CustomerOutVO();
        BeanUtils.copyProperties(customerDO, customerOutVO);
        return customerOutVO;
    }
}
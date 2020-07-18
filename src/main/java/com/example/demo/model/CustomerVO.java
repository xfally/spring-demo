package com.example.demo.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.dao.ds1.entity.CustomerDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户VO
 *
 * @author pax
 */
@ApiModel("客户VO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerVO implements Serializable {
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
    @Value("#{0}")
    private Integer age;

    @ApiModelProperty("描述")
    private String note;

    public static Page<CustomerVO> of(Page<CustomerDO> page) {
        if (page == null) {
            return new Page<>();
        }
        List<CustomerVO> customerVOList = page.getRecords()
            .stream()
            .map(CustomerVO::of)
            .collect(Collectors.toList());
        Page<CustomerVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(customerVOList);
        return pageOut;
    }

    public static CustomerVO of(CustomerDO customerDO) {
        if (customerDO == null) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customerDO, customerVO);
        return customerVO;
    }

    public static CustomerDO of(CustomerVO customerVO) {
        if (customerVO == null) {
            return null;
        }
        CustomerDO customerDO = new CustomerDO();
        BeanUtils.copyProperties(customerVO, customerDO);
        return customerDO;
    }
}

package io.github.xfally.spring.demo.model;

import io.github.xfally.spring.demo.dao.ds1.entity.CustomerDO;
import io.github.xfally.spring.demo.dao.ds1.entity.OrderDO;
import io.github.xfally.spring.demo.dao.ds1.entity.ProductDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 订单OutVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("订单OutVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderOutVO implements Serializable {
    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("客户ID")
    private Long customerId;

    @ApiModelProperty("产品ID")
    private Long productId;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("描述")
    private String note;

    @ApiModelProperty("客户VO")
    private CustomerOutVO customerOutVO;

    @ApiModelProperty("产品VO")
    private ProductOutVO productOutVO;

    public static OrderOutVO of(OrderDO orderDO, CustomerDO customerDO, ProductDO productDO) {
        if (orderDO == null) {
            return null;
        }
        OrderOutVO orderOutVO = new OrderOutVO();
        BeanUtils.copyProperties(orderDO, orderOutVO);
        orderOutVO.setCustomerOutVO(CustomerOutVO.of(customerDO));
        orderOutVO.setProductOutVO(ProductOutVO.of(productDO));
        return orderOutVO;
    }
}

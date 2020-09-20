package io.github.xfally.spring.demo.model;

import io.github.xfally.spring.demo.dao.ds1.entity.ProductDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 产品OutVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("产品OutVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductOutVO implements Serializable {
    @ApiModelProperty("产品ID")
    private Long id;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static ProductOutVO of(ProductDO productDO) {
        if (productDO == null) {
            return null;
        }
        ProductOutVO productOutVO = new ProductOutVO();
        BeanUtils.copyProperties(productDO, productOutVO);
        return productOutVO;
    }
}

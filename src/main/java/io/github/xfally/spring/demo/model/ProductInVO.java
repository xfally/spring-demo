package io.github.xfally.spring.demo.model;

import io.github.xfally.spring.demo.dao.ds1.entity.ProductDO;
import io.github.xfally.spring.demo.common.helper.Group4UpdateAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 产品InVO
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("产品InVO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductInVO implements Serializable {
    @ApiModelProperty("产品ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("产品名")
    @NotBlank(message = "产品名不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static ProductDO of(ProductInVO productInVO) {
        if (productInVO == null) {
            return null;
        }
        ProductDO productDO = new ProductDO();
        BeanUtils.copyProperties(productInVO, productDO);
        return productDO;
    }
}

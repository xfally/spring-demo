package com.example.demo.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.dao.ds1.entity.ProductDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品VO
 *
 * @author pax
 */
@ApiModel("产品VO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductVO implements Serializable {
    @ApiModelProperty("产品ID")
    @NotNull(message = "执行更新操作时，ID不可为NULL", groups = Group4UpdateAction.class)
    private Long id;

    @ApiModelProperty("产品名")
    @NotBlank(message = "产品名不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    public static Page<ProductVO> of(Page<ProductDO> page) {
        if (page == null) {
            return new Page<>();
        }
        List<ProductVO> productVOList = page.getRecords()
            .stream()
            .map(ProductVO::of)
            .collect(Collectors.toList());
        Page<ProductVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(productVOList);
        return pageOut;
    }

    public static ProductVO of(ProductDO productDO) {
        if (productDO == null) {
            return null;
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO, productVO);
        return productVO;
    }

    public static ProductDO of(ProductVO productVO) {
        if (productVO == null) {
            return null;
        }
        ProductDO productDO = new ProductDO();
        BeanUtils.copyProperties(productVO, productDO);
        return productDO;
    }
}

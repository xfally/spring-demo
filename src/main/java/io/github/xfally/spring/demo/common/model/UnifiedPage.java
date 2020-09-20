package io.github.xfally.spring.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 统一分页结果
 *
 * @author pax
 * @since 2020-03-19
 */
@ApiModel("统一分页结果")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnifiedPage<T> implements Serializable {

    @ApiModelProperty(value = "当前页码")
    private Long current;

    @ApiModelProperty(value = "每页数量")
    private Long size;

    @ApiModelProperty(value = "总数量")
    private Long total;

    @ApiModelProperty(value = "结果集合")
    private List<T> records;

    public static <T1, T2> UnifiedPage<T2> of(UnifiedPage<T1> page, List<T2> records) {
        UnifiedPage<T2> unifiedPage = new UnifiedPage<>();
        unifiedPage.setCurrent(page.getCurrent());
        unifiedPage.setSize(page.getSize());
        unifiedPage.setTotal(page.getTotal());
        unifiedPage.setRecords(records);
        return unifiedPage;
    }

    public static <T> UnifiedPage<T> ofMbp(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        UnifiedPage<T> unifiedPage = new UnifiedPage<>();
        unifiedPage.setCurrent(page.getCurrent());
        unifiedPage.setSize(page.getSize());
        unifiedPage.setTotal(page.getTotal());
        unifiedPage.setRecords(page.getRecords());
        return unifiedPage;
    }

    public static <T> UnifiedPage<T> ofJpa(org.springframework.data.domain.Page<T> page) {
        UnifiedPage<T> unifiedPage = new UnifiedPage<>();
        unifiedPage.setCurrent((long) page.getNumber());
        unifiedPage.setSize((long) page.getSize());
        unifiedPage.setTotal(page.getTotalElements());
        unifiedPage.setRecords(page.getContent());
        return unifiedPage;
    }

}

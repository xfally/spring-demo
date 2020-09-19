package io.github.xfally.springdemo.controller;


import io.github.xfally.springdemo.common.helper.Group4UpdateAction;
import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.common.response.UnifiedResponse;
import io.github.xfally.springdemo.dao.ds2.entity.ColorDO;
import io.github.xfally.springdemo.model.ColorInVO;
import io.github.xfally.springdemo.model.ColorOutVO;
import io.github.xfally.springdemo.service.IColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 颜色信息
 *
 * @author pax
 * @since 2020-05-08
 */
@Api(tags = {"颜色信息"})
@UnifiedResponse
@RestController
@RequestMapping("/color")
public class ColorController {
    @Autowired
    private IColorService colorService;

    @ApiOperation("获取颜色信息")
    @GetMapping("get")
    public ColorOutVO getColor(@ApiParam(value = "颜色ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return ColorOutVO.of(colorService.getColor(id));
    }

    @ApiOperation("获取所有颜色信息")
    @GetMapping("list")
    public List<ColorOutVO> listColors() {
        return colorService.listColors()
            .stream()
            .map(ColorOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询颜色信息")
    @PostMapping("query")
    public UnifiedPage<ColorOutVO> queryColors(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery,
                                               @ApiParam(value = "查询条件：颜色名") @RequestParam(required = false) @Valid String name) {
        UnifiedPage<ColorDO> page = colorService.queryColors(unifiedQuery, name);
        List<ColorOutVO> colorOutVOList = page.getRecords()
            .stream()
            .map(ColorOutVO::of)
            .collect(Collectors.toList());
        return UnifiedPage.of(page, colorOutVOList);
    }

    @ApiOperation("保存颜色信息")
    @PostMapping("save")
    public ColorOutVO saveColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Valid ColorInVO colorInVO) {
        return ColorOutVO.of(colorService.saveColor(ColorInVO.of(colorInVO)));
    }

    @ApiOperation("更新颜色信息")
    @PutMapping("update")
    public ColorOutVO updateColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ColorInVO colorInVO) {
        return ColorOutVO.of(colorService.updateColor(ColorInVO.of(colorInVO)));
    }

    @ApiOperation("删除颜色信息")
    @DeleteMapping("remove")
    public Boolean removeColor(@ApiParam(value = "颜色ID", required = true) @RequestParam @Valid @NotNull Long id) {
        return colorService.removeColor(id);
    }

}


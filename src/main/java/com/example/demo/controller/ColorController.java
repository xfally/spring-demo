package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds2.entity.ColorDO;
import com.example.demo.model.ColorInVO;
import com.example.demo.model.ColorOutVO;
import com.example.demo.service.IColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'color_' + #id")
    public ColorOutVO getColor(@ApiParam(value = "颜色ID", required = true) @RequestParam @Valid @NotNull Long id) {
        ColorDO colorDO = colorService.getById(id);
        if (colorDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return ColorOutVO.of(colorDO);
    }

    @ApiOperation("获取所有颜色信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'color_list'")
    public List<ColorOutVO> listColors() {
        List<ColorDO> colorDOList = colorService.list();
        if (colorDOList == null) {
            return new ArrayList<>();
        }
        return colorDOList
            .stream()
            .map(ColorOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页获取所有颜色信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<ColorOutVO> pageColors(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                       @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                       @ApiParam(value = "查询条件：颜色名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<ColorDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            lambdaQueryWrapper.like(ColorDO::getName, name);
        }
        Page<ColorDO> page = colorService.page(new Page<>(current, size), lambdaQueryWrapper);
        List<ColorOutVO> colorOutVOList = page.getRecords()
            .stream()
            .map(ColorOutVO::of)
            .collect(Collectors.toList());
        Page<ColorOutVO> page1 = new Page<>();
        BeanUtils.copyProperties(page, page1);
        page1.setRecords(colorOutVOList);
        return page1;
    }

    @ApiOperation("保存颜色信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'color_' + #result.id", condition = "#result.id != 'null'")
    public ColorOutVO saveColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Valid ColorInVO colorInVO) {
        ColorDO colorDO = ColorInVO.of(colorInVO);
        colorService.save(colorDO);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        return ColorOutVO.of(colorDO);
    }

    @ApiOperation("更新颜色信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'color_' + #result.id")
    public ColorOutVO updateColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ColorInVO colorInVO) {
        ColorDO colorDO = colorService.getById(colorInVO.getId());
        if (colorDO == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, colorInVO.getId());
        }
        colorDO = ColorInVO.of(colorInVO);
        colorService.updateById(colorDO);
        return ColorOutVO.of(colorDO);
    }

    @ApiOperation("删除颜色信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'color_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'color_list'", beforeInvocation = false)
        }
    )
    public Boolean removeColor(@ApiParam(value = "颜色ID", required = true) @RequestParam @Valid @NotNull Long id) {
        ColorDO colorDO = colorService.getById(id);
        ColorOutVO colorOutVO = new ColorOutVO();
        if (colorDO == null) {
            colorOutVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return colorService.removeById(id);
    }

}


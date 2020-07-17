package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.dao.ds2.entity.Color;
import com.example.demo.model.ColorVO;
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
import java.util.LinkedList;
import java.util.List;

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
    public ColorVO getColor(@ApiParam(value = "颜色ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Color color = colorService.getById(id);
        ColorVO colorVO = new ColorVO();
        if (color == null) {
            colorVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        BeanUtils.copyProperties(color, colorVO);
        return colorVO;
    }

    @ApiOperation("获取所有颜色信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'color_list'")
    public List<ColorVO> listColors() {
        List<Color> colors = colorService.list();
        if (colors == null) {
            return new ArrayList<>();
        }
        List<ColorVO> colorVOList = new LinkedList<>();
        for (Color color : colors) {
            ColorVO colorVO = new ColorVO();
            BeanUtils.copyProperties(color, colorVO);
            colorVOList.add(colorVO);
        }
        return colorVOList;
    }

    @ApiOperation("分页获取所有颜色信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<ColorVO> pageColors(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                    @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                    @ApiParam(value = "查询条件：颜色名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<Color> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(name)) {
            lambdaQueryWrapper.like(Color::getName, name);
        }
        Page<Color> page = colorService.page(new Page<>(current, size), lambdaQueryWrapper);
        if (page == null) {
            return new Page<>();
        }
        List<ColorVO> colorVOList = new LinkedList<>();
        for (Color color : page.getRecords()) {
            ColorVO colorVO = new ColorVO();
            BeanUtils.copyProperties(color, colorVO);
            colorVOList.add(colorVO);
        }
        Page<ColorVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(colorVOList);
        return pageOut;
    }

    @ApiOperation("保存颜色信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'color_' + #result.id", condition = "#result.id != 'null'")
    public ColorVO saveColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Valid ColorVO colorVO) {
        Color color = new Color();
        BeanUtils.copyProperties(colorVO, color);
        colorService.save(color);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        colorVO.setId(color.getId());
        return colorVO;
    }

    @ApiOperation("更新颜色信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'color_' + #result.id")
    public ColorVO updateColor(@ApiParam(value = "颜色信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) ColorVO colorVO) {
        Color color = colorService.getById(colorVO);
        if (color == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1004, colorVO);
        }
        BeanUtils.copyProperties(colorVO, color);
        colorService.updateById(color);
        return colorVO;
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
        Color color = colorService.getById(id);
        ColorVO colorVO = new ColorVO();
        if (color == null) {
            colorVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1004, id);
        }
        return colorService.removeById(id);
    }

}


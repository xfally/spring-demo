package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.entity.Group;
import com.example.demo.model.GroupVO;
import com.example.demo.service.IGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * 组信息
 *
 * @author pax
 * @since 2020-05-08
 */
@Api(tags = {"组信息"})
@UnifiedResponse
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private IGroupService groupService;

    @ApiOperation("获取组信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'group_' + #id")
    public GroupVO getGroup(@ApiParam(value = "组ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Group group = groupService.getById(id);
        GroupVO groupVO = new GroupVO();
        if (group == null) {
            groupVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        BeanUtils.copyProperties(group, groupVO);
        return groupVO;
    }

    @ApiOperation("获取所有组信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'group_list'")
    public List<GroupVO> listGroups() {
        List<Group> groups = groupService.list();
        if (groups == null) {
            return new ArrayList<>();
        }
        List<GroupVO> groupVOList = new LinkedList<>();
        for (Group group : groups) {
            GroupVO groupVO = new GroupVO();
            BeanUtils.copyProperties(group, groupVO);
            groupVOList.add(groupVO);
        }
        return groupVOList;
    }

    @ApiOperation("分页获取所有组信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<GroupVO> pageGroups(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                    @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                    @ApiParam(value = "查询条件：组名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<Group> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Group::getName, name);
        Page<Group> page = groupService.page(new Page<>(current, size), lambdaQueryWrapper);
        if (page == null) {
            return new Page<>();
        }
        List<GroupVO> groupVOList = new LinkedList<>();
        for (Group group : page.getRecords()) {
            GroupVO groupVO = new GroupVO();
            BeanUtils.copyProperties(group, groupVO);
            groupVOList.add(groupVO);
        }
        Page<GroupVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(groupVOList);
        return pageOut;
    }

    @ApiOperation("保存组信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'group_' + #result.id", condition = "#result.id != 'null'")
    public GroupVO saveGroup(@ApiParam(value = "组信息", required = true) @RequestBody @Valid GroupVO groupVO) {
        Group group = new Group();
        BeanUtils.copyProperties(groupVO, group);
        groupService.save(group);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        groupVO.setId(group.getId());
        return groupVO;
    }

    @ApiOperation("更新组信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'group_' + #result.id")
    public GroupVO updateGroup(@ApiParam(value = "组信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) GroupVO groupVO) {
        Group group = groupService.getById(groupVO);
        if (group == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1002, groupVO);
        }
        BeanUtils.copyProperties(groupVO, group);
        groupService.updateById(group);
        return groupVO;
    }

    @ApiOperation("删除组信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'group_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'group_list'", beforeInvocation = false)
        }
    )
    public Boolean removeGroup(@ApiParam(value = "组ID", required = true) @RequestParam @Valid @NotNull Long id) {
        Group group = groupService.getById(id);
        GroupVO groupVO = new GroupVO();
        if (group == null) {
            groupVO.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1002, id);
        }
        return groupService.removeById(id);
    }

}


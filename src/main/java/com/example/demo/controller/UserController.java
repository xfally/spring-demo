package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.helper.Group4UpdateAction;
import com.example.demo.common.response.UnifiedCodeEnum;
import com.example.demo.common.response.UnifiedException;
import com.example.demo.common.response.UnifiedResponse;
import com.example.demo.entity.User;
import com.example.demo.model.UserVO;
import com.example.demo.service.IUserService;
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
 * 用户信息
 * <p>
 * 前端控制器
 * </p>
 * 用`@Transactional`修饰类，则所有方法都启用事务。
 * 方法也可以同时加该注解，将优先采用方法的注解配置。
 * 如果觉得影响性能，可不给类注解，只给部分方法加事务注解。
 * <p>
 * 不用`@Controller`，而是用`@RestController`注解修饰类后，
 * 则所有的方法默认都返回JSON，XML或自定义mediaType内容到页面（即被`@ResponseBody`注解的效果），
 * 至于具体是哪种，MessageConverter接口的实现类会根据返回内容灵活判断。
 *
 * @author pax
 * @since 2020-03-19
 */
@Api(tags = {"用户信息"})
@UnifiedResponse
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping("get")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'user_' + #id")
    public UserVO getUser(@ApiParam(value = "用户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        User user = userService.getById(id);
        UserVO userVo = new UserVO();
        if (user == null) {
            userVo.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @ApiOperation("获取所有用户信息")
    @GetMapping("list")
    @Cacheable(value = "demoCache", condition = "#result != 'null'", key = "'user_list'")
    public List<UserVO> listUsers() {
        List<User> users = userService.list();
        if (users == null) {
            return new ArrayList<>();
        }
        List<UserVO> userVOList = new LinkedList<>();
        for (User user : users) {
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            userVOList.add(userVo);
        }
        return userVOList;
    }

    @ApiOperation("分页获取所有用户信息")
    @GetMapping("page")
    // 因为有搜索条件，命中率低，不采用缓存
    public Page<UserVO> pageUsers(@ApiParam(value = "当前页码") @RequestParam(defaultValue = "1") @Valid @NotNull Long current,
                                  @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") @Valid @NotNull Long size,
                                  @ApiParam(value = "查询条件：用户名") @RequestParam(required = false) @Valid String name) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, name);
        Page<User> page = userService.page(new Page<>(current, size), lambdaQueryWrapper);
        if (page == null) {
            return new Page<>();
        }
        List<UserVO> userVOList = new LinkedList<>();
        for (User user : page.getRecords()) {
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            userVOList.add(userVo);
        }
        Page<UserVO> pageOut = new Page<>();
        BeanUtils.copyProperties(page, pageOut);
        pageOut.setRecords(userVOList);
        return pageOut;
    }

    @ApiOperation("保存用户信息")
    @PostMapping("save")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'user_' + #result.id", condition = "#result.id != 'null'")
    public UserVO saveUser(@ApiParam(value = "用户信息", required = true) @RequestBody @Valid UserVO userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        userService.save(user);
        // 测试事务回滚，查看数据库以验证效果
        //int a = 1 / 0;
        userVo.setId(user.getId());
        return userVo;
    }

    @ApiOperation("更新用户信息")
    @PutMapping("update")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CachePut(value = "demoCache", key = "'user_' + #result.id")
    public UserVO updateUser(@ApiParam(value = "用户信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) UserVO userVo) {
        User user = userService.getById(userVo);
        if (user == null) {
            throw new UnifiedException(UnifiedCodeEnum.B1001, userVo);
        }
        BeanUtils.copyProperties(userVo, user);
        userService.updateById(user);
        return userVo;
    }

    @ApiOperation("删除用户信息")
    @DeleteMapping("remove")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @Caching(
        evict = {
            @CacheEvict(value = "demoCache", key = "'user_' + #id", beforeInvocation = false),
            @CacheEvict(value = "demoCache", key = "'user_list'", beforeInvocation = false)
        }
    )
    public Boolean removeUser(@ApiParam(value = "用户ID", required = true) @RequestParam @Valid @NotNull Long id) {
        User user = userService.getById(id);
        UserVO userVo = new UserVO();
        if (user == null) {
            userVo.setId(id);
            throw new UnifiedException(UnifiedCodeEnum.B1001, id);
        }
        return userService.removeById(id);
    }

}


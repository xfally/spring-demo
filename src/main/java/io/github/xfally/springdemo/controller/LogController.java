package io.github.xfally.springdemo.controller;


import io.github.xfally.springdemo.common.helper.Group4UpdateAction;
import io.github.xfally.springdemo.common.model.UnifiedPage;
import io.github.xfally.springdemo.common.model.UnifiedQuery;
import io.github.xfally.springdemo.common.response.UnifiedResponse;
import io.github.xfally.springdemo.dao.ds0.entity.LogDO;
import io.github.xfally.springdemo.model.LogInVO;
import io.github.xfally.springdemo.model.LogOutVO;
import io.github.xfally.springdemo.service.ILogService;
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
 * 日志信息
 *
 * @author pax
 * @since 2020-08-31
 */
@Api(tags = {"日志信息"})
@UnifiedResponse
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private ILogService logService;

    @ApiOperation("获取日志信息")
    @GetMapping("get")
    public LogOutVO getLog(@ApiParam(value = "日志ID", required = true) @RequestParam @Valid @NotNull String id) {
        return LogOutVO.of(logService.getLog(id));
    }

    @ApiOperation("获取所有日志信息")
    @GetMapping("list")
    public List<LogOutVO> listLogs() {
        return logService.listLogs()
            .stream()
            .map(LogOutVO::of)
            .collect(Collectors.toList());
    }

    @ApiOperation("分页查询日志信息")
    @PostMapping("query")
    public UnifiedPage<LogOutVO> queryLogs(@ApiParam(value = "统一查询条件") @RequestBody @Valid @NotNull UnifiedQuery unifiedQuery,
                                           @ApiParam(value = "查询条件：日志名") @RequestParam(required = false) @Valid String name,
                                           @ApiParam(value = "查询条件：日志等级") @RequestParam(required = false) @Valid String level) {
        UnifiedPage<LogDO> page = logService.queryLogs(unifiedQuery, name, level);
        List<LogOutVO> logOutVOList = page.getRecords()
            .stream()
            .map(LogOutVO::of)
            .collect(Collectors.toList());
        return UnifiedPage.of(page, logOutVOList);
    }

    @ApiOperation("保存日志信息")
    @PostMapping("save")
    public LogOutVO saveLog(@ApiParam(value = "日志信息", required = true) @RequestBody @Valid LogInVO logInVO) {
        return LogOutVO.of(logService.saveLog(LogInVO.of(logInVO)));
    }

    @ApiOperation("更新日志信息")
    @PutMapping("update")
    public LogOutVO updateLog(@ApiParam(value = "日志信息", required = true) @RequestBody @Validated(Group4UpdateAction.class) LogInVO logInVO) {
        return LogOutVO.of(logService.updateLog(LogInVO.of(logInVO)));
    }

    @ApiOperation("删除日志信息")
    @DeleteMapping("remove")
    public Boolean removeLog(@ApiParam(value = "日志ID", required = true) @RequestParam @Valid @NotNull String id) {
        return logService.removeLog(id);
    }

}


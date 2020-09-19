package io.github.xfally.springdemo.controller;

import com.alibaba.fastjson.JSON;
import io.github.xfally.springdemo.common.response.UnifiedResponse;
import io.github.xfally.springdemo.model.MsgVO;
import io.github.xfally.springdemo.service.IMQService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 消息队列
 *
 * @author pax
 * @since 2020-03-19
 */
@Api(tags = {"消息队列"})
@UnifiedResponse
@RestController
@RequestMapping("/mq")
public class MQController {
    @Autowired
    private IMQService rabbitMQService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public MsgVO sendMsg(@ApiParam(value = "消息", required = true) @RequestBody @Valid MsgVO msgVO) {
        String msg = JSON.toJSONString(msgVO);
        rabbitMQService.sendMsg(msg);
        return msgVO;
    }

}

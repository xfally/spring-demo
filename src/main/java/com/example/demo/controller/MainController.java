package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 主页
 *
 * @author pax
 * @since 2020-05-09
 */
@Api(tags = {"主页"})
@Controller
@RequestMapping("/")
public class MainController {
    @Value("${server.port}")
    private String port;
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    @Value("${spring.rabbitmq.web-port}")
    private String rabbitmqPort;
    @Value("${eureka.address}")
    private String eurekaAddress;

    @ApiOperation("主页")
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @ApiOperation("主页")
    @GetMapping("/")
    public String main() throws UnknownHostException {
        return "redirect:/tip";
    }

    @ApiOperation("提示")
    @GetMapping("/tip")
    @ResponseBody
    public String tip() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        String swaggerUrl = "http://" + ip + ":" + port + "/swagger-ui.html";
        String actuatorUrl = "http://" + ip + ":" + port + "/actuator";
        String druidUrl = "http://" + ip + ":" + port + "/druid";
        String rabbitmqUrl = "http://" + rabbitmqHost + ":" + rabbitmqPort;
        String eurekaUrl = "http://" + eurekaAddress;
        return String.format("可以访问以下内容：<br>" +
                "接口测试（Swagger2）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "服务" +
                "监控（Actuator）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "数据库请求监控（Druid）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "消息队列中间件（RabbitMQ）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "服务治理（Eureka）: <a href=\"%s\" target=\"_blank\">%s</a><br>",
            swaggerUrl, swaggerUrl,
            actuatorUrl, actuatorUrl,
            druidUrl, druidUrl,
            rabbitmqUrl, rabbitmqUrl,
            eurekaUrl, eurekaUrl);
    }
}

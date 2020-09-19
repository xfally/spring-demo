package io.github.xfally.springdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
    @Value("${server.ip}")
    private String ip;
    @Value("${server.port}")
    private String port;
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    @Value("${spring.rabbitmq.web-port}")
    private String rabbitmqPort;
    @Value("${eureka.address}")
    private String eurekaAddress;
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

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
        if (StringUtils.isBlank(ip)) {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        }
        String swaggerUrl = "http://" + ip + ":" + port + "/swagger-ui.html";
        String actuatorUrl = "http://" + ip + ":" + port + "/actuator";
        String druidUrl = "http://" + ip + ":" + port + "/druid";
        String rabbitmqUrl = "http://" + rabbitmqHost + ":" + rabbitmqPort;
        String eurekaUrl = "http://" + eurekaAddress;
        return String.format("可以访问以下内容：<br>" +
                "接口测试（Swagger2）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "服务监控（Actuator）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "数据库请求监控（Druid）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "消息队列中间件（RabbitMQ）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "服务治理（Eureka）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "单点登录服务（Keycloak）: <a href=\"%s\" target=\"_blank\">%s</a><br>" +
                "（登录后想登出，请访问单点登录服务的后台管理页面，在Sessions板块进行操作。）",
            swaggerUrl, swaggerUrl,
            actuatorUrl, actuatorUrl,
            druidUrl, druidUrl,
            rabbitmqUrl, rabbitmqUrl,
            eurekaUrl, eurekaUrl,
            keycloakUrl, keycloakUrl);
    }
}

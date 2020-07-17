package com.example.demo.common.response;

import lombok.Getter;
import lombok.ToString;

/**
 * 统一错误码
 *
 * @author pax
 */
@ToString
public enum UnifiedCodeEnum {
    //=== 成功 ===//
    SUCCESS("00000", "成功"),
    FAIL("10000", "失败"),
    //=== 用户端错误 ===//
    A0001("A0001", "用户端错误"),
    A0100("A0100", "用户注册错误"),
    A0200("A0200", "用户登陆异常"),
    A0201("A0201", "用户账户不存在"),
    A0220("A0220", "用户身份校验失败"),
    A0300("A0300", "访问权限异常"),
    A0400("A0400", "用户请求参数错误"),
    A0410("A0410", "请求必填参数为空"),
    A0500("A0500", "用户请求服务异常"),
    A0600("A0600", "用户资源异常"),
    A0700("A0700", "用户上传文件异常"),
    A0800("A0800", "用户当前版本异常"),
    A0900("A0900", "用户隐私未授权"),
    A1000("A1000", "用户设备异常"),
    //=== 系统执行出错 ===//
    B0001("B0001", "系统执行出错"),
    B0100("B0100", "系统执行超时"),
    B0200("B0200", "系统容灾功能被触发"),
    B0300("B0300", "系统资源异常"),
    // 业务错误（该应用私有），从这里开始定义
    B1000("B1000", "业务错误"),
    B1001("B1001", "用户不存在"),
    B1002("B1002", "组不存在"),
    //=== 调用第三方服务出错 ===//
    C0001("C0001", "调用第三方服务出错"),
    C0100("C0100", "中间件服务出错"),
    C0200("C0200", "第三方系统执行超时"),
    C0300("C0300", "数据库服务出错"),
    C0400("C0400", "第三方容灾系统被触发"),
    C0500("C0500", "通知服务出错"),
    ;

    UnifiedCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private String code;
    @Getter
    private String msg;
}

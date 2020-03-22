package com.example.demo.common.response;

import lombok.Getter;
import lombok.ToString;

/**
 * @author pax
 */

@ToString
public enum UnifiedCodeEnum {
    //=== 通用错误 ===//
    // 成功
    SUCCESS(0, "成功。"),
    // 失败
    FAIL(-1, "失败！"),
    // 参数错误
    ARG_ERROR(-2, "参数错误！"),
    // 缺少认证信息
    AUTH_LACK_ERROR(-200, "缺少认证信息！"),
    // 认证失败
    AUTH_FAIL_ERROR(-201, "认证失败！"),
    //=== 业务错误 ===//
    // 分门别类，各占一个区间
    USER_NOT_EXIST(-1000, "用户不存在！"),
    ;

    UnifiedCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private Integer code;
    @Getter
    private String msg;
}

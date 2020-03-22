package com.example.demo.common.response;

import lombok.Getter;
import lombok.ToString;

/**
 * @author pax
 */

@ToString
public enum UnifiedResultEnum {
    // 成功
    SUCCESS(true),
    // 失败
    FAIL(false),
    ;

    UnifiedResultEnum(Boolean result) {
        this.result = result;
    }

    @Getter
    private Boolean result;
}
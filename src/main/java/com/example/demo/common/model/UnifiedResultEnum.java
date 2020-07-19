package com.example.demo.common.model;

import lombok.Getter;
import lombok.ToString;

/**
 * 统一结果值
 *
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
    private final Boolean result;
}
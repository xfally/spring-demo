package com.example.demo.common.response;

import lombok.Getter;

/**
 * 统一异常
 *
 * @author pax
 */
public class UnifiedException extends RuntimeException {
    @Getter
    private final String message;
    @Getter
    private final UnifiedResponseDTO<Object> unifiedResponseDTO;

    public UnifiedException(UnifiedCodeEnum unifiedCodeEnum) {
        unifiedResponseDTO = UnifiedResponseFactory.createFail(unifiedCodeEnum);
        message = unifiedResponseDTO.getMsg() + " {" + unifiedResponseDTO.getData() + "}";
    }

    public UnifiedException(UnifiedCodeEnum unifiedCodeEnum, Object data) {
        unifiedResponseDTO = UnifiedResponseFactory.createFail(unifiedCodeEnum, data);
        message = unifiedResponseDTO.getMsg() + " {" + unifiedResponseDTO.getData() + "}";
    }

    public UnifiedException(Throwable cause, UnifiedCodeEnum unifiedCodeEnum) {
        super(cause);
        unifiedResponseDTO = UnifiedResponseFactory.createFail(unifiedCodeEnum);
        message = unifiedResponseDTO.getMsg() + " {" + unifiedResponseDTO.getData() + "}";
    }
}

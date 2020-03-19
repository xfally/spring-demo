package com.example.demo.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pax
 */
@Data
public class UnifiedResponseDTO<T> implements Serializable {
    private Boolean result;
    private Integer code;
    private String msg;
    private T data;
    private Long timestamp;
}

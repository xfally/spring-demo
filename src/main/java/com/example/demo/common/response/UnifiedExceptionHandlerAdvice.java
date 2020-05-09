package com.example.demo.common.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author pax
 */
@ControllerAdvice(annotations = UnifiedResponse.class)
@ResponseBody
@Slf4j
public class UnifiedExceptionHandlerAdvice {
    /**
     * 处理未捕获的 Exception
     *
     * @param e 异常
     * @return 统一响应体
     */
    @ExceptionHandler(Exception.class)
    public UnifiedResponseDTO<Object> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return UnifiedResponseFactory.createFail(e.getMessage());
    }

    /**
     * 处理未捕获的 RuntimeException
     *
     * @param e 运行时异常
     * @return 统一响应体
     */
    @ExceptionHandler(RuntimeException.class)
    public UnifiedResponseDTO<Object> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return UnifiedResponseFactory.createFail(e.getMessage());
    }

    /**
     * 处理未捕获的 MethodArgumentNotValidException
     *
     * @param e 参数异常
     * @return 统一响应体
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public UnifiedResponseDTO<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return UnifiedResponseFactory.createFail(UnifiedCodeEnum.ARG_ERROR, e.getMessage());
    }

    /**
     * 处理业务异常 UnifiedException
     *
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(UnifiedException.class)
    public UnifiedResponseDTO<Object> handleUnifiedException(UnifiedException e) {
        log.error(e.getMessage(), e);
        return e.getUnifiedResponseDTO();
    }
}

package io.github.xfally.spring.demo.common.response;

import io.github.xfally.spring.demo.common.model.UnifiedResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一返回处理
 *
 * @author pax
 * @since 2020-03-19
 */
@ControllerAdvice(annotations = UnifiedResponse.class)
@Slf4j
public class UnifiedResponseHandlerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.debug("beforeBodyWrite...");
        Object oo;
        // 判断响应的Content-Type为JSON格式的o
        if (MediaType.APPLICATION_JSON.equals(mediaType) || MediaType.APPLICATION_JSON_UTF8.equals(mediaType)) {
            if (o instanceof UnifiedResponseDTO) {
                // 如果响应返回的对象为统一响应体，则直接返回o
                oo = o;
            } else {
                // 只有正常返回的结果才会进入这个判断流程，所以返回正常成功的状态码
                oo = UnifiedResponseFactory.createSuccess(o);
            }
        } else {
            // 非JSON格式o直接返回即可
            oo = o;
        }
        log.debug("Resp={}", oo);
        return oo;
    }
}

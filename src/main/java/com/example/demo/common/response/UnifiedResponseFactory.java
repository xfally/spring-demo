package com.example.demo.common.response;

/**
 * 统一返回构建工厂
 *
 * @author pax
 */
public class UnifiedResponseFactory {
    private UnifiedResponseFactory() {
    }

    public static <T> UnifiedResponseDTO<T> createSuccess() {
        return createResponse(UnifiedResultEnum.SUCCESS.getResult(), UnifiedCodeEnum.SUCCESS.getCode(), UnifiedCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <T> UnifiedResponseDTO<T> createSuccess(T data) {
        return createResponse(UnifiedResultEnum.SUCCESS.getResult(), UnifiedCodeEnum.SUCCESS.getCode(), UnifiedCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <T> UnifiedResponseDTO<T> createFail() {
        return createResponse(UnifiedResultEnum.FAIL.getResult(), UnifiedCodeEnum.FAIL.getCode(), UnifiedCodeEnum.FAIL.getMsg(), null);
    }

    public static <T> UnifiedResponseDTO<T> createFail(UnifiedCodeEnum unifiedCodeEnum) {
        return createResponse(UnifiedResultEnum.FAIL.getResult(), unifiedCodeEnum.getCode(), unifiedCodeEnum.getMsg(), null);
    }

    public static <T> UnifiedResponseDTO<T> createFail(String msg) {
        return createResponse(UnifiedResultEnum.FAIL.getResult(), UnifiedCodeEnum.FAIL.getCode(), msg, null);
    }

    public static <T> UnifiedResponseDTO<T> createFail(String msg, T data) {
        return createResponse(UnifiedResultEnum.FAIL.getResult(), UnifiedCodeEnum.FAIL.getCode(), msg, data);
    }

    public static <T> UnifiedResponseDTO<T> createFail(UnifiedCodeEnum unifiedCodeEnum, T data) {
        return createResponse(UnifiedResultEnum.FAIL.getResult(), unifiedCodeEnum.getCode(), unifiedCodeEnum.getMsg(), data);
    }

    public static <T> UnifiedResponseDTO<T> createResponse(Boolean result, Integer code, String msg, T data) {
        UnifiedResponseDTO<T> unifiedResponseDTO = new UnifiedResponseDTO<>();
        unifiedResponseDTO.setResult(result);
        unifiedResponseDTO.setCode(code);
        unifiedResponseDTO.setMsg(msg);
        unifiedResponseDTO.setData(data);
        unifiedResponseDTO.setTimestamp(System.currentTimeMillis());
        return unifiedResponseDTO;
    }

}

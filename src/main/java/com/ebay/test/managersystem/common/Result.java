package com.ebay.test.managersystem.common;

import lombok.Data;

/**
 * Common Response Structure
 * @param <T>
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ReturnCode.RC200.getCode());
        result.setMsg(ReturnCode.RC200.getMsg());
        result.setData(null);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ReturnCode.RC200.getCode());
        result.setMsg(ReturnCode.RC200.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(ReturnCode returnCode) {
        Result<T> result = new Result<>();
        result.setCode(returnCode.getCode());
        result.setMsg(returnCode.getMsg());
        result.setData(null);
        return result;
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

}

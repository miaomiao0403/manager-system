package com.ebay.test.managersystem.exception;

import com.ebay.test.managersystem.common.ReturnCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizationException extends RuntimeException{
    private int code;
    private String msg;

    public AuthorizationException() {}

    public AuthorizationException(ReturnCode returnCode) {
        super(returnCode.getMsg());
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
    }

    public AuthorizationException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}

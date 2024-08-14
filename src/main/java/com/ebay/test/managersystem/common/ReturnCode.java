package com.ebay.test.managersystem.common;

import lombok.Getter;

@Getter
public enum ReturnCode {
    RC200(200, "ok"),
    RC400(400, "Parameter Error"),
    RC401(401, "Unauthorized"),
    RC403(403, "Forbidden"),
    RC404(404, "Not Found"),
    RC405(405, "Method Not Allowed"),
    RC500(500, "Internal Server Error。");

    private final int code;

    private final String msg;

    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

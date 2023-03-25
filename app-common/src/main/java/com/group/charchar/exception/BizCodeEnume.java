package com.group.charchar.exception;

/**
 * 错误码和错误信息定义类
 * 错误码列表:
 * 10:通用
 *      001:参数格式校验
 *      002:短信验证频率太高
 */
public enum BizCodeEnume {

    UNKNOW_EXCEPTION(10000, "unknow exception"),
    VALID_EXCEPTION(10001, "valid exception"),
    TO_MANY_REQUEST(10002, "request too much");

    private int code;
    private String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

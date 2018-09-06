package com.gemo.common.net;

/**
 * Created by DELL on 2018年2月2日 002.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * HttpResultException: 自定义Http返回结果异常
 */

public class HttpResultException extends RuntimeException {

    private int code;
    private String msg;

    public HttpResultException() {
        super();
    }

    public HttpResultException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public HttpResultException(int code, String msg) {
        super(code + "," + msg);
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

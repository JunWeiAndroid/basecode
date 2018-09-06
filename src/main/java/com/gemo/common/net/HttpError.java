package com.gemo.common.net;

/**
 * Created by DELL on 2018年6月2日 002.
 * E-Mail:n.zjx@163.com
 * Android
 * HttpError: 网络请求情况
 */
public enum HttpError {

    /**
     * 没有错误
     */
    OK,

    /**
     * 错误
     */
    ERROR,

    /**
     * 连接超时
     */
    TIMEOUT,

    /**
     * 返回结果非200错误
     */
    RESULT_ERROR,

    /**
     * 连接错误
     */
    CONNECT_ERROR,

    /**
     * 数据（格式）解析错误
     */
    DATA_PARSE_ERROR,

    /**
     * 需要重新登录
     */
    NEED_RELOGIN;

    private String value;

    HttpError() {
    }

    HttpError(String value) {
        this.value = value;
    }

}

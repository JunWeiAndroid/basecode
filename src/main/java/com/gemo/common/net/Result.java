package com.gemo.common.net;

import android.support.annotation.Nullable;

/**
 * Created by DELL on 2018年5月14日 014.
 * E-Mail:n.zjx@163.com
 * Android
 * Result: 返回结果基类
 */
public class Result<T> {

    public int code;//结果码
    public String msg;//请求返回信息
    @Nullable
    public T data;//结果数据

    @Override
    public String toString() {
        return "Result{" + "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.gemo.common.net;

import java.util.List;

/**
 * Created by DELL on 2018年5月14日 014.
 * E-Mail:n.zjx@163.com
 * Android
 * Pager: 有关列表分页基类
 */
public class Pager<T> {

    public int pageNo;//页码
    public int pageSize;//返回列表项数量
    public int total;//总数量
    public List<T> list;//返回列表

    @Override
    public String toString() {
        return "Pager{" + "page=" + pageNo +
                ", limit=" + pageSize +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}

package com.gemo.common.base;

/**
 * Created by WJW
 * BaseView: MVP V层基类
 */

public interface BaseView {

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载动画
     */
    void hideLoading();

    /**
     * 显示消息
     * @param msg 消息
     */
    void showMsg(String msg);
}

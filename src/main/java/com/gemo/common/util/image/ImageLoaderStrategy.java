package com.gemo.common.util.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by DELL on 2018年3月30日 030.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * ImageLoaderStrategy: 图片加载策略接口定义
 */

public interface ImageLoaderStrategy {

    /**
     * 清理图片缓存
     */
    void clearCache(Context context);

    /**
     * 清理图片内存
     */
    void clearMemory(Context context);

    /**
     * 调整图片内存
     */
    void trimMemory(Context context, int level);

    void load(Context context, @DrawableRes int resId, @DrawableRes int placeholder, ImageView imageView);

    /**
     * @param context
     * @param url
     * @param placeholder 图片资源
     * @param imageView
     */
    void load(Context context, String url, @DrawableRes int placeholder, ImageView imageView);

    /**
     * @param context
     * @param url       图片url
     * @param imageView
     */
    void load(Context context, String url, ImageView imageView);

    void loadDefaultScale(Context context, String url, ImageView imageView);

    /**
     * @param context
     * @param url
     * @param holder    图片加载前显示的图片
     * @param error     图片加载失败显示的图片
     * @param imageView
     */
    void load(Context context, String url, @DrawableRes int holder, @DrawableRes int error, ImageView imageView);

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url       需要加载的图片资源地址
     * @param holder    图片加载出来前显示的图片
     * @param imageView
     */
    void loadCircle(Context context, String url, @DrawableRes int holder, ImageView imageView);

    /**
     * @param context
     * @param url       需要加载的图片地址
     * @param holder    图片加载出来前现实的图片
     * @param radius    圆角半径 像素
     * @param imageView
     */
    void loadRounded(Context context, String url, @DrawableRes int holder, int radius, ImageView imageView);


    void loadGif(Context context, @DrawableRes int res, ImageView imageView);


    void loadInside(Context context, String url, ImageView imageView);

    void loadWithBlank(Context context, String url, ImageView imageView);

}

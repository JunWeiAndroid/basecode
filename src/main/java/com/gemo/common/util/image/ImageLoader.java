package com.gemo.common.util.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;


/**
 * Created by DELL on 2017年6月16日 016.
 * 图片加载工具类
 */

public class ImageLoader {

    private static ImageLoader sInstance;
    private ImageLoaderStrategy loaderStrategy;

    private ImageLoader() {
        //具体使用什么图片加载框架加载根据具体需求实现
        loaderStrategy = new GlideImageLoaderStrategy();
//        loaderStrategy = new PicassoImageLoaderStrategy();
    }

    public synchronized static ImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public void clearCache(Context context) {
        loaderStrategy.clearCache(context);
    }

    public void clearMemory(Context context){
        loaderStrategy.clearMemory(context);
    }

    public void trimMemory(Context context, int level){
        loaderStrategy.trimMemory(context, level);
    }

    public void load(Context context, @DrawableRes int resId, @DrawableRes int placeholder, ImageView imageView) {
        loaderStrategy.load(context, resId, placeholder, imageView);
    }

    public void loadInside(Context context, String url, ImageView imageView) {
        loaderStrategy.loadInside(context, url, imageView);
    }

    /**
     * @param context
     * @param url
     * @param placeholder 图片资源
     * @param imageView
     */
    public void load(Context context, String url, @DrawableRes int placeholder, ImageView imageView) {
        loaderStrategy.load(context, url, placeholder, imageView);

    }

    /**
     * @param context
     * @param url       图片url
     * @param imageView
     */
    public void load(Context context, String url, ImageView imageView) {
        loaderStrategy.load(context, url, imageView);
    }

    /**
     * Load default scale.
     *
     * @param context   the context
     * @param url       the link
     * @param imageView the image view
     */
    public void loadDefaultScale(Context context, String url, ImageView imageView) {
        loaderStrategy.loadDefaultScale(context, url, imageView);
    }

    /**
     * @param context
     * @param url
     * @param holder    图片加载前显示的图片
     * @param error     图片加载失败显示的图片
     * @param imageView
     */
    public void load(Context context, String url, @DrawableRes int holder, @DrawableRes int error, ImageView imageView) {
        loaderStrategy.load(context, url, holder, error, imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url       需要加载的图片资源地址
     * @param holder    图片加载出来前显示的图片
     * @param imageView
     */
    public void loadCircle(Context context, String url, @DrawableRes int holder, ImageView imageView) {
        loaderStrategy.loadCircle(context, url, holder, imageView);
    }

    /**
     * @param context
     * @param url       需要加载的图片地址
     * @param holder    图片加载出来前现实的图片
     * @param radius    圆角半径 像素
     * @param imageView
     */
    public void loadRounded(Context context, String url, @DrawableRes int holder, int radius, ImageView imageView) {
        loaderStrategy.loadRounded(context, url, holder, radius, imageView);
    }

    public void loadGif(Context context, @DrawableRes int res, ImageView imageView) {
        loaderStrategy.loadGif(context, res, imageView);
    }

    public void loadWithBlank(Context context, String url, ImageView imageView) {
        loaderStrategy.loadWithBlank(context, url, imageView);
    }
}

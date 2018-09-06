package com.gemo.common.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

/**
 * Created by DELL on 2018年3月30日 030.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * GlideImageLoaderStrategy: 图片加载策略具体实现
 * 使用Glide实现
 * https://github.com/bumptech/glide
 * <p>
 * 需要切换其他图片加载框架请再写一个类实现{@link ImageLoaderStrategy} ,
 * 然后在具体调用类 创建改实例
 */

public class GlideImageLoaderStrategy implements ImageLoaderStrategy {

    @Override
    public void clearCache(Context context) {
        GlideApp.get(context).clearDiskCache();
    }

    @Override
    public void clearMemory(Context context) {
        GlideApp.get(context).clearMemory();
    }

    @Override
    public void trimMemory(Context context, int level) {
        GlideApp.get(context).trimMemory(level);
    }

    @Override
    public void load(Context context, @DrawableRes int resId, @DrawableRes int placeholder, ImageView imageView) {
        getGlideRequests(context, placeholder)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView);

    }

    /**
     * @param context
     * @param url
     * @param placeholder 图片资源
     * @param imageView
     */
    @Override
    public void load(Context context, String url, @DrawableRes int placeholder, ImageView imageView) {
        getGlideRequests(context, url)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView);

    }

    /**
     * @param context
     * @param url       图片url
     * @param imageView
     */
    @Override
    public void load(Context context, String url, ImageView imageView) {
        getGlideRequests(context, url)
                .centerCrop()
                .into(imageView);

    }

    /**
     * @param context
     * @param url       图片url
     * @param imageView
     */
    @Override
    public void loadDefaultScale(Context context, String url, ImageView imageView) {
        getGlideRequests(context, url).into(imageView);
    }

    /**
     * @param context
     * @param url
     * @param holder    图片加载前显示的图片
     * @param error     图片加载失败显示的图片
     * @param imageView
     */
    @Override
    public void load(Context context, String url, @DrawableRes int holder, @DrawableRes int error, ImageView imageView) {
        getGlideRequests(context, url)
                .placeholder(holder)
                .error(error)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url       需要加载的图片资源地址
     * @param holder    图片加载出来前显示的图片
     * @param imageView
     */
    @Override
    public void loadCircle(Context context, String url, @DrawableRes int holder, ImageView imageView) {
        getGlideRequests(context, url)
                .placeholder(holder)
                .circleCrop()
                .into(imageView);
    }

    /**
     * @param context
     * @param url       需要加载的图片地址
     * @param holder    图片加载出来前现实的图片
     * @param radius    圆角半径 像素
     * @param imageView
     */
    @Override
    public void loadRounded(Context context, String url, @DrawableRes int holder, int radius, ImageView imageView) {
        getGlideRequests(context, url)
                .placeholder(holder)
                .circleCrop()
                .optionalTransform(new RoundedCorners(radius))
                .into(imageView);
    }

    @Override
    public void loadGif(Context context, @DrawableRes int url, ImageView imageView) {
        GlideApp.with(context)
                .asGif()
                .load(url)
                .centerCrop()
                .into(imageView);

    }

    public GlideRequest<Drawable> getGlideRequests(Context context, Object url) {
        return GlideApp.with(context)
                .load(url);
    }

    public GlideRequest<Bitmap> getGlideRequestsBitmap(Context context, Object url) {
        return GlideApp.with(context).asBitmap()
                .load(url);
    }

    @Override
    public void loadInside(Context context, String url, ImageView imageView) {
        getGlideRequests(context, url)
                .centerInside()
                .into(imageView);
    }

    @Override
    public void loadWithBlank(Context context, String url, ImageView imageView) {
        getGlideRequests(context, url)
                .into(imageView);
    }

}

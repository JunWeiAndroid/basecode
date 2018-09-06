package com.gemo.common.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by DELL on 2018年6月6日 006.
 * E-Mail:n.zjx@163.com
 * Android
 * ImageUtils: 图片工具类
 */
public class ImageUtils {

    private ImageUtils() {

    }

    /**
     * 等比例缩放Bitmap 大小
     *
     * @param bitmap 原始图片
     * @param scale  缩放比例
     * @return 缩放后的图片
     */
    public static Bitmap resizeImage(Bitmap bitmap, @FloatRange(from = 0.1f) float scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * Drawable 颜色转化类
     *
     * @param drawable
     * @param color    资源
     * @return 改变颜色后的Drawable
     */
    public static Drawable tintDrawable(@NonNull Drawable drawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }


}

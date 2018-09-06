package com.gemo.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.gemo.common.CommonConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by DELL on 2017年6月16日 016.
 * Add by hzg on 2017/12/12
 * 尺寸转换工具类
 */

public class SizeUtil {
    public static float dp2px(Context context, int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, context.getResources().getDisplayMetrics());
    }

    public static float dp2px(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, CommonConfig.getContext().getResources().getDisplayMetrics());
    }

    public static int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, CommonConfig.getContext().getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;

    }

    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;

    }

    //获取适应全屏幕的图片大小
    public static Point getBitmapSize(String path, Activity activity) {

        Point imageSize = getBitmapBound(path);
        int w = imageSize.x;
        int h = imageSize.y;
//        if (PhotoMetadataUtils.shouldRotate(resolver, uri)) {
//            w = imageSize.y;
//            h = imageSize.x;
//        }
//        if (h == 0) return new Point(MAX_WIDTH, MAX_WIDTH);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenWidth = (float) metrics.widthPixels;
        float screenHeight = (float) metrics.heightPixels;
        float widthScale = screenWidth / w;
        float heightScale = screenHeight / h;
        if (widthScale > heightScale) {
            return new Point((int) (w * widthScale), (int) (h * heightScale));
        }
        return new Point((int) (w * widthScale), (int) (h * heightScale));
    }

    //获取图片的宽高
    public static Point getBitmapBound(String path) {
        InputStream is = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            is = new FileInputStream(path);
            BitmapFactory.decodeStream(is, null, options);
            int width = options.outWidth;
            int height = options.outHeight;
            return new Point(width, height);
        } catch (FileNotFoundException e) {
            return new Point(0, 0);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

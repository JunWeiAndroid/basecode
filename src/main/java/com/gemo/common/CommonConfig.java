package com.gemo.common;

import android.annotation.SuppressLint;
import android.content.Context;

import com.gemo.common.net.HeaderData;
import com.gemo.common.util.Logger;
import com.gemo.common.util.image.cache.GlideImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;

/**
 * Created by WJW on 2018/8/10.
 */

public class CommonConfig {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static HeaderData headerData;
    public static final String SEPARATOR = ";";//分隔符
    public static String baseUrl;


    public static void init(Context c, String hostUrl, HeaderData header) {
        baseUrl = hostUrl;
        context = c;
        headerData = header;
        SImagePicker.init(new PickerConfig.Builder().setAppContext(c)
                .setImageLoader(new GlideImageLoader())
                .build());
    }

    public static Context getContext() {
        if (context == null) {
            throw new RuntimeException("Please call CommonConfig.init() method at fisrt.");
        }
        return context;
    }

    public static void initHeaderData(HeaderData header) {
        headerData = header;
    }

    public static HeaderData getHeaderData() {
        if (headerData == null) {
            Logger.e("HeaderData has been recycled, please check.");
        }
        return headerData;
    }

}

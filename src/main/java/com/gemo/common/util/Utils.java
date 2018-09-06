package com.gemo.common.util;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by DELL on 2018年8月23日 023.
 * E-Mail:n.zjx@163.com
 * BookStadium
 * Utils:
 */
public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static String SP_FILE_NAME = "";

    public static void init(Context context) {
        sContext = context;
    }

    public static void initSpFileName(String fileName) {
        SP_FILE_NAME = fileName;
    }

    public static String getSpFileName() {
        return SP_FILE_NAME;
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new NullPointerException("请先初始化");
        }
        return sContext;
    }
}

package com.gemo.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gemo.common.CommonConfig;

import java.util.Set;

/**
 * SharePreferences 工具类
 */

public class SPUtil {

    private static final String SP_FILE_NAME = "book_stadium_sp";

    private static SharedPreferences getSP() {
        return CommonConfig.getContext().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getSP().edit();
    }

    public static void putBoolean(@NonNull String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(@NonNull String key) {
        return getSP().getBoolean(key, false);
    }

    public static boolean getBoolean(@NonNull String key, boolean def) {
        return getSP().getBoolean(key, def);
    }

    public static void putString(@NonNull String key, @Nullable String value) {
        getEditor().putString(key, value).apply();
    }

    public static void putLong(String key, long longValue) {
        getEditor().putLong(key, longValue).apply();
    }

    public static long getLong(String key, long def) {
        return getSP().getLong(key, def);
    }

    public static String getString(@NonNull String key) {
        return getSP().getString(key, "");
    }

//    public static Set<String> getStringSet(@NonNull String key) {
//        return getSP().getStringSet(key, new ArraySet<>(0));
//    }

    public static void removeKey(@NonNull String key) {
        getEditor().remove(key).apply();
    }

    public static void putStringSet(@NonNull String key, Set<String> values) {
        getEditor().putStringSet(key, values).apply();
    }

    public static void clean(@NonNull String key) {
        getEditor().remove(key).apply();
    }

    public static void clear(){
        getEditor().clear().apply();
    }
}

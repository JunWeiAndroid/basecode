package com.gemo.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gemo.common.CommonConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2018年1月12日 012.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * StringUtil: 字符串操作相关工具类
 */

public class StringUtil {

    private StringUtil() {

    }

    public static final Gson GSON = new GsonBuilder().create();

    /**
     * 格式化String
     *
     * @param source  需要格式化的字符串
     * @param objects 放入字符串的参数
     * @return
     */
    public static String format(String source, Object... objects) {
        return String.format(Locale.getDefault(), source, objects);
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return obj 的 json
     */
    public static String obj2Json(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * json 转对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * json转对象
     *
     * @param json Gson jsonObject
     * @param type 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(JsonObject json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * json转List
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T[]> type) {
        T[] array = GSON.fromJson(json, type);
        return Arrays.asList(array);
    }

    /**
     * 从parentJson获取key对应的json字符串
     */
    public static String getJsonByKey(@NonNull String parentJson, @NonNull String key) {
        return getStringFromJson(GSON.fromJson(parentJson, JsonObject.class), key);

    }

    /**
     * null 转"”
     *
     * @param s source
     * @return "" or s
     */
    public static String trimNull(String s) {
        return s == null ? "" : s;
    }

    /**
     * @param object Gson 的JsonObject
     * @param key    键
     * @return value or null
     */
    public static String getStringFromJson(JsonObject object, String key) {
        return object.has(key) ? object.get(key).getAsString() : null;
    }

    /**
     * @param object object Gson 的JsonObject
     * @param key    键
     * @return value or 0
     */
    public static int getIntFromJson(JsonObject object, String key) {
//        int i;
        try {
//            i=Integer.parseInt(getStringFromJson(object,key));
            return object.get(key).getAsInt();
        } catch (Exception e) {
//            i=0;
            return 0;
        }
//        return object.has(key) ?/* object.get(key).getAsInt()*/i : 0;
    }

    /**
     * String List 转成由 ; 拼接的字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(list.get(i));
            if (i != list.size() - 1) {
                stringBuffer.append(CommonConfig.SEPARATOR);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * Arrays.asList 的封装,如果参数只有一个,就使用Collections.singletonList
     *
     * @param objects 参数
     * @param <T>     类型
     * @return List<T>
     */
    @SafeVarargs
    public static <T> List<T> asList(T... objects) {
        if (objects.length == 1) {
            return Collections.singletonList(objects[0]);
        } else {
            return Arrays.asList(objects);
        }
    }


    private static final Pattern IS_INTEGER_PATTERN = Pattern.compile("^[-+]?[\\d]*$");

    /**
     * 判断是否为整数
     * @param str 数字字符串
     * @return true 整数
     */
    public static boolean isInteger(String str) {
        return IS_INTEGER_PATTERN.matcher(str).matches();
    }

    /**
     * 数字字符串转换，如果为int字符串，直接原样返回，如果包含小数点，小数超过2位，保留2位小数返回，否则原样返回
     * @param originalString
     * @return
     */
    public static String stringToNumberString(String originalString) {
        int vaildDigital = 2;//小数点后有效位数
        if (TextUtils.isEmpty(originalString) || !RegexUtils.isNumber(originalString)) {// 字符串为空，或者字符串不是数字，原样返回，不做处理
            return originalString;
        }
        int sLength = originalString.length();
        int dotIndex = originalString.indexOf(".");
        if (dotIndex < 0) {//不包含小数点，即该字符串对应为int，直接返回即可
            return originalString;
        }
        if (dotIndex + vaildDigital >= sLength - 1) {//包含小数点，但不足2位小数，原样返回
            return originalString;
        } else { // 超过两位小数，保留2位小数
            return String.format(Locale.getDefault(), "%.2f", Double.parseDouble(originalString.substring(0, dotIndex + vaildDigital + 1 + 1)));
        }
    }

}

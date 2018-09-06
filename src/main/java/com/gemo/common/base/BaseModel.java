package com.gemo.common.base;

import android.support.annotation.NonNull;

import com.gemo.common.util.StringUtil;
import com.google.gson.JsonObject;

import java.text.MessageFormat;

/**
 * Created by WJW
 * BaseModel: MVP m层基类
 */

public abstract class BaseModel implements IBaseData{

    /**
     * JsonObject 转 对象
     * 有Error直接抛异常，onError会接收， 可在onFailure处理
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    protected  <T> T json2Obj(JsonObject object, Class<T> clazz) {
        //返回正常数据的先决条件
//        if (object.has(KEY_ERROR) && object.size() == 1) {
//            throw new HttpResultException(StringUtil.getStringFromJson(object, KEY_ERROR));
//        }
        return StringUtil.json2Obj(object, clazz);
    }

    /**
     * 有Error直接抛异常，onError会接收， 可在onFailure处理
     * @param jsonObject 返回的json数据
     * @return 1：true
     */
    @NonNull
    protected Integer getIntegerSuccess(JsonObject jsonObject) {
//        if (jsonObject.has(KEY_ERROR) && jsonObject.size() == 1) {
//            throw new HttpResultException(StringUtil.getStringFromJson(jsonObject, KEY_ERROR));
//        }
//        return StringUtil.getIntFromJson(jsonObject, KEY_SUCCESS);
        return 1;
    }


    /**
     * @param format     字符串key
     * @param index      序号索引
     * @return 整型数
     */
    protected int getIntFromJson(JsonObject jsonObject, MessageFormat format, Integer[] index) {
        return StringUtil.getIntFromJson(jsonObject, format.format(index));
    }

    /**
     * @param jsonObject    json对象
     * @param messageFormat key
     * @param index         需要索引
     * @return 字符串
     */
    protected String getStringFromJson(JsonObject jsonObject, MessageFormat messageFormat, Integer[] index) {
        return StringUtil.getStringFromJson(jsonObject, messageFormat.format(index));
    }
}

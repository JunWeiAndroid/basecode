package com.gemo.common.util;

import android.text.TextUtils;

/** TextUtil
 * Created by WJW on 2018/5/23.
 */

public class TextUtil {

    public static boolean isEmpty(String text){
        return TextUtils.isEmpty(text) || text.equalsIgnoreCase("null");
    }

}

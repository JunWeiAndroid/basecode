package com.gemo.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则判断类
 * Created by WJW on 2018/5/24.
 */

public class RegexUtils {

    private static final String REGEX_EMOJI;

    static {
        REGEX_EMOJI = "(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?)";
    }

    //        private static final String REGEX_PHONE = "^((17[0-9])|(13[0-9])|(14[0-9])|(15([0-9]))|(18[0-9]))\\d{8}$";
    private static final String REGEX_PHONE = "^1\\d{10}";
    //    private static final String REGEX_PHONE = "^1\\d{10}$";
    private static final String REGEX_WORDS_NUM = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";//中英文数字
    private static final String REGEX_SMS_CODE = "^\\d{5}$";
    private static final String REGEX_NUMBER = "^-?[0-9]*\\.?[0-9]*$";

    /**
     * 判断该字符串是否为手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        return isMatchRegex(phone, REGEX_PHONE);
    }


    /**
     * 判断该字符串是否为符合规则的短信验证码
     *
     * @param smsCode
     * @return
     */
    public static boolean isSmsCode(String smsCode) {
        return true ? true : isMatchRegex(smsCode, REGEX_SMS_CODE);
    }

    /**
     * 判断是否有emoji表情
     */
    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile(REGEX_EMOJI);
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    /**
     * 中英文数字
     */
    public static boolean isMatchesWords(String name) {
        return isMatchRegex(name, REGEX_WORDS_NUM);
    }


    private static boolean isMatchRegex(String text, String regex) {
        return !TextUtil.isEmpty(text) && text.matches(regex);
    }

    public static boolean isNumber(String string) {
        return isMatchRegex(string, REGEX_NUMBER);
    }

}

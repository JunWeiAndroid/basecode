package com.gemo.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DELL on 2018年9月5日 005.
 * E-Mail:n.zjx@163.com
 * BookStadium
 * EncryptUtil: 加解密工具类
 */
public class EncryptUtil {

    /**
     * 字符串转MD5
     */
    public static String toMD5(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(str.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String tmp = Integer.toHexString(b & 0xff);
                if (tmp.length() == 1) {
                    tmp = '0' + tmp;
                }
                builder.append(tmp);
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}

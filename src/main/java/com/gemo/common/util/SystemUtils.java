package com.gemo.common.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by WJW on 2018/8/15.
 */

public class SystemUtils {

    private static final String PERMISSION_TEL = Manifest.permission.CALL_PHONE;

    /**
     * 跳转至拨号页面
     * @param context c
     * @param phoneNumber p
     */
    public static void callTelDial(Context context, String phoneNumber){
        if (PermissionUtil.hasPermissions(context, PERMISSION_TEL)) {
            Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
            context.startActivity(dialIntent);
        }else {
            PermissionUtil.requestPermissions(context, 0x1001, PERMISSION_TEL);
        }
    }

    /**
     * 调用系统分享
     * @param context
     * @param title
     * @param content
     * @param dialogTitle
     */
    public static void systemShare(Context context, String title, String content, String dialogTitle) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
        share_intent = Intent.createChooser(share_intent, dialogTitle);
        context.startActivity(share_intent);
    }


}

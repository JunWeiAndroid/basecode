package com.gemo.common.util.image;

import android.Manifest;
import android.app.Activity;

import com.gemo.common.base.BaseFragment;
import com.gemo.common.util.PermissionUtil;
import com.gemo.common.util.image.cache.CacheManager;
import com.imnjh.imagepicker.SImagePicker;

/** 图片选取工具类
 */

public class SelectImageUtils {

    private static final String WRITE_SD = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_SD = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;

    /**
     * 打开相册选取单张图片    使用方法参考EditPersonalInfoActivity.class
     * @param isShowCamera 是否显示拍照按钮
     * @param activity 调用的activity
     * @param cacheName 选取图片后换成的文件名称
     * @param requestCode 请求码
     */
    public static void selectSingleImage(boolean isShowCamera, Activity activity, String cacheName, int requestCode){
        if (PermissionUtil.hasPermissions(activity, WRITE_SD, READ_SD, CAMERA)) {
            SImagePicker
                    .from(activity)
                    .pickMode(SImagePicker.MODE_AVATAR)
                    .showCamera(isShowCamera)
                    .cropFilePath(
                            CacheManager.getInstance().getImageInnerCache()
                                    .getAbsolutePath(cacheName))
                    .forResult(requestCode);
        }else {
            PermissionUtil.requestPermissions(activity, 101, WRITE_SD, READ_SD, CAMERA);
        }
    }

    public static void selectSingleImage(boolean isShowCamera, BaseFragment fragment, String cacheName, int requestCode){
        if (PermissionUtil.hasPermissions(fragment.getContext(), WRITE_SD, READ_SD, CAMERA)) {
            SImagePicker
                    .from(fragment)
                    .pickMode(SImagePicker.MODE_AVATAR)
                    .showCamera(isShowCamera)
                    .cropFilePath(
                            CacheManager.getInstance().getImageInnerCache()
                                    .getAbsolutePath(cacheName))
                    .forResult(requestCode);
        }else {
            PermissionUtil.requestPermissions(fragment, 101, WRITE_SD, READ_SD, CAMERA);
        }
    }



}

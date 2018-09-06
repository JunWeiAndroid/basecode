package com.gemo.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.gemo.common.CommonConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018年5月16日 016.
 * E-Mail:n.zjx@163.com
 * Android
 * PermissionUtil: 商城权限工具类
 * 不支持{@link android.app.Fragment}
 */
public class PermissionUtil {

    private static final int APP_SETTINGS_RC = 0x111;

    /**
     * Find denied permission list.
     *
     * @param context    the context
     * @param permission the permission
     * @return the list 被拒绝的权限
     */
    public static List<String> findDeniedPermissions(Context context, @Size(min = 1) String... permission) {
        List<String> denyPermission = new ArrayList<>(permission.length);
        for (String value : permission) {
            if (ActivityCompat.checkSelfPermission(context, value) != PackageManager.PERMISSION_GRANTED) {
                denyPermission.add(value);
            }
        }
        return denyPermission;
    }

    /**
     * 检查是否所以权限都通过
     * true:所以权限都通过
     *
     * @param context
     * @param permission
     * @return true:所以权限都通过
     */
    public static boolean hasPermissions(Context context, @Size(min = 1) String... permission) {
        if (isAndroidM()) {
            for (String value : permission) {
                if (ActivityCompat.checkSelfPermission(context, value) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * Request permission.
     *
     * @param obj         the obj {@link Activity} 或者 {@link Fragment}
     * @param requestCode the request code
     * @param permissions the permissions
     */
    public static void requestPermissions(Object obj, int requestCode, String... permissions) {
        if (!isAndroidM()) {
            return;
        }
        Context context = getContext(obj);
        List<String> deniedPermissions = findDeniedPermissions(context, permissions);

        if (deniedPermissions.size() > 0) {//有被拒绝的权限
            String[] array = deniedPermissions.toArray(new String[deniedPermissions.size()]);
            if (obj instanceof Activity) {
                ((Activity) obj).requestPermissions(array, requestCode);
            } else if (obj instanceof Fragment) {
                ((Fragment) obj).requestPermissions(array, requestCode);
            } else {
                throw new IllegalArgumentException(obj.getClass().getName() + " is not support");
            }
        }
    }

    /**
     *
     * 是否需要说明权限的作用，一般用户选择“不再提示”需要，相当于永久拒绝
     *
     * @param obj        the the obj {@link Activity} 或者 {@link Fragment}
     * @param permissions the permission
     * @return the boolean true:“不再提示”
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkedPermanentlyDenied(Object obj, List<String> permissions) {
        for (String value : permissions) {
            if (!showRequestPermissionRationale(obj, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@link Activity#shouldShowRequestPermissionRationale}
     * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。
     * 如果设备规范禁止应用具有该权限，此方法也会返回 false。
     * @param obj the obj {@link Activity} 或者 {@link Fragment}
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean showRequestPermissionRationale(Object obj, String permission) {
        if (obj instanceof Activity) {
            return ((Activity) obj).shouldShowRequestPermissionRationale(permission);
        } else if (obj instanceof Fragment) {
            return ((Fragment) obj).shouldShowRequestPermissionRationale(permission);
        } else {
            throw new IllegalArgumentException(obj.getClass().getName() + " is not support");
        }
    }

    private static Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getContext();
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not support");
        }
    }

    /**
     * @return true:android 6.0
     */
    private static boolean isAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void openSetting(Object obj) {
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", CommonConfig.getContext().getPackageName(), null)),
                    APP_SETTINGS_RC);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", CommonConfig.getContext().getPackageName(), null)),
                    APP_SETTINGS_RC);
        }
    }


}

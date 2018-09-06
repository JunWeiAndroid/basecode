package com.gemo.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.gemo.common.util.DialogUtils;
import com.gemo.common.util.Logger;
import com.gemo.common.util.PermissionUtil;
import com.gemo.common.util.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WJW
 * BaseActivity
 * 子类不需要Presenter则不用带泛型
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P mPresenter;
    protected View mView;
    protected Dialog loadingDialog;
    protected boolean isNeedInitDialog = true;

//    protected ImmersionBar mImmersionBar;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.i("currentActivity:" + this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(getLayoutResId(), null);
        setContentView(mView);
        if (isImmersionBarEnable()) {
            initStatusBar();
        }
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
        if (isNeedInitDialog) {
            loadingDialog = DialogUtils.createLoadingDialog(this);
        }
        initViews(savedInstanceState);
        initData();
        initListeners();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    private void initStatusBar() {
//        mImmersionBar = ImmersionBar.with(this)
//                .statusBarColor(R.color.bg_white)
////                .navigationBarColor(R.color.bg_white)//navigationBar
//                .statusBarDarkFont(true)
//                .fitsSystemWindows(true)
//                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        mImmersionBar.init();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void startAct(@NonNull Class<? extends AppCompatActivity> c) {
        startActivity(new Intent(this, c));
    }

    public void startAct(Class<? extends AppCompatActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转activity，并关闭所有打开的activity， 暂用于退出登录后跳转到登录页
     *
     * @param clazz
     * @param bundle
     */
    public void startActAndCloseOtherAllAct(Class<? extends AppCompatActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Start activity.
     * 动画跳转
     */
    public void startActivity(Class<? extends AppCompatActivity> clazz, Bundle bundle, View sharedElement, String sharedElementName) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, sharedElementName).toBundle());
    }

    /**
     * Start activity.
     * 动画跳转
     */
    public void startActivity(Class<? extends AppCompatActivity> clazz, View sharedElement, String sharedElementName) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, sharedElementName).toBundle());
    }

    public void startActForResult(@NonNull Class c, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(this, c);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected View findView(@IdRes int id) {
        return findViewById(id);
    }

    /**
     * @param <T> 如果BaseActivity子类不需要Presenter泛型，
     *            此方法获取ViewDataBinding需要强转或者在子类直接使用DataBindingUtil.bind(mView)
     * @return
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return DataBindingUtil.bind(mView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.i(this.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i(this.getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i(this.getClass().getSimpleName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.i(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        Logger.i(this.getClass().getSimpleName());
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
//        if (mImmersionBar != null) {
//            mImmersionBar.destroy();
//        }
        super.onDestroy();
    }

    //按返回键结束当前activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> granted = new ArrayList<>();// 允许的权限
        List<String> denied = new ArrayList<>();//拒绝的权限

        for (int i = 0, k = permissions.length; i < k; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permissions[i]);
            } else {
                denied.add(permissions[i]);
            }
        }

        //允许权限
        if (granted.size() > 0) {
            onPermissionGranted(requestCode, granted);
        }

        //拒绝权限
        if (denied.size() > 0) {
            onPermissionsDenied(requestCode, denied);
        }

        //所有权限通过
        if (granted.size() > 0 && denied.size() == 0) {
            onAllPermissionsGranted(requestCode);
        }

    }

    /**
     * 所以权限通过
     */
    protected void onAllPermissionsGranted(int requestCode) {

    }

    /**
     * 被拒绝的权限，一般需要提示用户，比如
     * 1、拒绝后重新继续申请权限
     * 2、用户点击了“不再提醒”则提示手动去开启
     * 3、被拒绝的权限不重要，不用管了
     */
    protected void onPermissionsDenied(int requestCode, List<String> denied) {
        //默认判断是否需要提示用户手动开启权限
        if (PermissionUtil.checkedPermanentlyDenied(this, denied)) {
            DialogUtils.showConfirmDialog(BaseActivity.this, "权限提示", "请设置相关权限，点击\"确定\"进行设置",
                    "取消", null,
                    "确定", (dialog, which) -> {
                        PermissionUtil.openSetting(BaseActivity.this);
                    });
        }
    }

    /**
     * 通过的权限，一般不用管
     */
    protected void onPermissionGranted(int requestCode, List<String> granted) {

    }

    /**
     * 是否要显示状态栏
     *
     * @return
     */
    protected boolean isImmersionBarEnable() {
        return true;
    }

    /**
     * @return bundle
     */
    protected Bundle getExtraBundle() {
        return getIntent().getExtras();
    }

    protected void toastS(@NonNull String info) {
        ToastUtil.toastS(info);
    }

    protected void toastS(@StringRes int info) {
        ToastUtil.toastS(info);
    }

    protected void toastL(@NonNull String info) {
        ToastUtil.toastL(info);
    }

    protected void toastL(@StringRes int info) {
        ToastUtil.toastL(info);
    }

    /**
     * 子类重写此方法初始化presenter
     *
     * @return presenter
     */
    protected P initPresenter() {
        return null;
    }

    protected abstract int getLayoutResId();

    protected abstract void initViews(@Nullable Bundle savedInstanceState);

    protected abstract void initData();

    protected void initListeners() {

    }

    @Override
    public void showLoading() {
        Logger.e("显示loading " + getClass().getName());
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.createLoadingDialog(this);
        }
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        toastS(msg);
    }

}

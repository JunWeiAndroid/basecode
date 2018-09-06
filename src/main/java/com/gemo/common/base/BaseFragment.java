package com.gemo.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemo.common.util.DialogUtils;
import com.gemo.common.util.Logger;
import com.gemo.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WJW
 * BaseFragment: Fragment基类
 * 子类不需要Presenter则不用带泛型
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    protected P mPresenter;

    protected Context mContext;
    protected View mView;
    protected Dialog loadingDialog;
    protected boolean isCreateView;//是否初始化View完成
    protected boolean isVisible;//是否处于可见状态
    protected boolean isFistLoad = true;//是否第一次加载

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = DialogUtils.createLoadingDialog(mContext);
    }

    public abstract int getLayoutResId();

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (!isCreateView) {
            mPresenter = initPresenter();
            if (mPresenter != null) {
                mPresenter.attach(this);
            }
            initViews(savedInstanceState);
            isCreateView = true;
            initData();
            initListener();
            checkLazyLoadData();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutResId(), container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 子类重写此方法初始化presenter
     *
     * @return presenter
     */
    protected P initPresenter() {
        return null;
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    protected void initViews(@Nullable Bundle savedInstanceState) {
    }

    /**
     * @param <T> 如果BaseActivity子类不需要Presenter泛型，
     *            此方法获取ViewDataBinding需要强转或者在子类直接使用DataBindingUtil.bind(mView)
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) (DataBindingUtil.bind(mView));
    }

    /**
     * 延迟加载，页面可见时才加载数据
     */
    private void checkLazyLoadData() {
        Logger.i(this.getClass().getSimpleName() + ": isFirstLoad=" + isFistLoad + "  isCreateView=" + isCreateView + "  isVisible=" + isVisible);

        if (!isFistLoad || !isCreateView || !isVisible) {
            return;
        }
        Logger.d("加载数据(" + this.getClass().getSimpleName() + ")");
        isFistLoad = false;
        lazyLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {//界面对用户可见
            isVisible = true;
            checkLazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
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
        //默认判断是否需要提示用户手动开启权限,activity已经回调了，fragment不再需要
        /*if (PermissionUtil.checkedPermanentlyDenied(this, denied)) {
            DialogUtils.showConfirmDialog(mContext, "权限提示", "请设置相关权限，点击\"确定\"进行设置",
                    "取消", null,
                    "确定", (dialog, which) -> {
                        PermissionUtil.openSetting(BaseFragment.this);
                    });
        }*/
    }

    /**
     * 通过的权限，一般不用管
     */
    protected void onPermissionGranted(int requestCode, List<String> granted) {

    }

    @Override
    public void onDestroy() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {
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
    /**
     * 等页面可见时再加载，并且加载一次。如果需要每次页面可见都加载的话，把{isFistLoad 在加载是设置为true}
     */
    protected void lazyLoadData() {

    }

    public void startAct(@NonNull Class c) {
        startActivity(new Intent(getContext(), c));
    }

    public void startActForResult(@NonNull Class c, int requestCode) {
        startActivityForResult(new Intent(getContext(), c), requestCode);
    }

    public void startActForResult(@NonNull Class c, int requestCode, @Nullable Bundle bundle) {
        Intent intent = new Intent(getActivity(), c);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void startAct(Class<? extends AppCompatActivity> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivity(intent);
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

    protected <V extends View> V findView(@IdRes int id) {
        return mView.findViewById(id);
    }

}

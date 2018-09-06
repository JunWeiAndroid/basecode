package com.gemo.common.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gemo.common.R;
import com.gemo.common.util.SizeUtil;

import java.util.Objects;

/**
 * Created by WJW
 * BaseDialog: Dialog 基类
 * 可以很好解决其他弹框的问题：例如popupWindow不能使用EditText,Dialog生命周期管理等
 */

public class BaseDialog extends DialogFragment {

    protected Context mContext;
    protected View mView;

    protected NestedScrollView mContainerView;

    public int mLayoutId;
    private boolean isFullScreen;//是否全屏
    private int mGravity = Gravity.NO_GRAVITY;//位置
    protected int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;
    @FloatRange(from = 0.0f, to = 1.0f)
    protected float mRelativeWidth, mRelativeHeight = 0f;//宽比例(0-1] 高
    private int mTheme = android.R.style.Theme_Holo_Light_Dialog_MinWidth;//dialog样式

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initDialog();
        if (isFullScreen) {
            mTheme = R.style.dialog_full_screen;
        }
        Dialog dialog = new Dialog(mContext, mTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(mGravity);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutId == 0) {
            mLayoutId = getLayoutId();
            if (mLayoutId == 0) {
                return super.onCreateView(inflater, container, savedInstanceState);
            }
        }
        mContainerView = new NestedScrollView(mContext);
        mContainerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (mView == null) {
//            mView = inflater.inflate(mLayoutId, mContainerView, false);
            mView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(mLayoutId, mContainerView, false);
        }

        mContainerView.addView(mView);

        return mContainerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isFullScreen) {//可设置宽度，高度不设置
            WindowManager.LayoutParams params = getWindow().getAttributes();
            int maxH =SizeUtil.getDisplayHeight(mContext);
            int width = Math.min(SizeUtil.getDisplayWidth(mContext), maxH);
            if (mRelativeWidth != 0 && mRelativeHeight != 0) {
                params.height = (int) (maxH * mRelativeHeight);
                params.width = (int) (width * mRelativeWidth);
                getWindow().setAttributes(params);
            } else if (mRelativeWidth == 0 && mRelativeHeight != 0) {
                params.height = (int) (maxH * mRelativeHeight);
                params.width = mWidth;
                getWindow().setAttributes(params);
            } else if (mRelativeWidth != 0 && mRelativeHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = (int) (width * mRelativeWidth);
                getWindow().setAttributes(params);
            } else {
                getWindow().setLayout(mWidth, WindowManager.LayoutParams.WRAP_CONTENT);
            }

        }
        init();
    }

    protected <B extends ViewDataBinding> B getDataBinding() {
        return DataBindingUtil.bind(mView);
    }

    @Override
    public View getView() {
        return mContainerView;
    }

    /**
     * Sets layout id.
     *
     * @param layoutId the layout id
     */
    public void setLayoutId(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
    }

    /**
     * @param theme 默认系统最小宽度dialog
     */
    public void setTheme(int theme) {
        if (theme != 0) {
            this.mTheme = theme;
        }
        setStyle(STYLE_NO_TITLE, theme);
    }

    /**
     * 重写设置layoutId
     *
     * @return 布局id
     */
    @LayoutRes
    public int getLayoutId() {
        return 0;
    }

    /**
     * 设置是否全屏，默认不全屏
     */
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    /**
     * 设置位置
     *
     * @param gravity 位置{@link Gravity}
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * 设置宽度是否为matchParent
     */
    public void setMatchParentWidth() {
        this.mWidth = WindowManager.LayoutParams.MATCH_PARENT;
    }

    /**
     * mType:this.getClass().getSimpleName()
     */
    public void show(FragmentManager manager) {
        if (!isAdded()) {
            try {
                super.show(manager, this.getClass().getSimpleName());
            }catch (Exception exception) {
                //IllegalStateException 在 onSaveInstanceState 方法后调用show方法可能会出现 IllegalStateException 异常，可以重写DialogFragment
                //的show方法，ft.commit() 改为 ft.commitAllowingStateLoss()即可，commitAllowingStateLoss会忽略状态检查，避免异常，这里简单try catch处理了
                // https://www.jianshu.com/p/f6570ce9e413
                exception.printStackTrace();
            }
        }
    }

    /**
     * 初始化Dialog的样式,比如设置全屏，位置等.比{@link #init()}先执行
     *
     * @see #init()
     */
    protected void initDialog() {

    }

    /**
     * 初始化，比如初始化view，设置监听，设置数据.
     * 初始化Dialog之后调用
     *
     * @see #initDialog()
     */
    protected void init() {

    }

    /**
     * 需要在初始化dialog后调用，例如在init()方法里调用
     * Sets dialog background color.
     * reference {@link Color}
     *
     * @param color the color {@link Color} or other color int
     */
    protected void setBackgroundColor(@ColorInt int color) {
        getWindow().setBackgroundDrawable(new ColorDrawable(color));
    }

    public Window getWindow() {
        return getDialog().getWindow();
    }

    /**
     * BaseDialog 简单构建
     */
    public static class Builder {

        @LayoutRes
        private int mLayoutId;
        private boolean isFullScreen;//是否全屏
        private int mGravity = Gravity.NO_GRAVITY;//位置
        @FloatRange(from = 0.0f, to = 1.0f)
        private float mRelativeWidth, mRelativeHeight = 0f;//宽比例(0-1]
        private int mTheme = android.R.style.Theme_Holo_Light_Dialog_MinWidth;//dialog样式
        @ColorInt
        private int mBgColor = -1;

        public BaseDialog build() {
            BaseDialog dialog = new BaseDialog();
            dialog.setFullScreen(isFullScreen);
            dialog.setGravity(mGravity);
            dialog.mTheme = mTheme;
            if (mLayoutId != 0) {
                dialog.setLayoutId(mLayoutId);
            }
            if (mRelativeWidth > 0) {
                dialog.mRelativeWidth = mRelativeWidth;
            }
            if (mRelativeHeight > 0) {
                dialog.mRelativeHeight = mRelativeHeight;
            }
            if (mBgColor != -1) {
                dialog.setBackgroundColor(mBgColor);
            }
            return dialog;
        }

        public Builder setTheme(@StyleRes int theme) {
            this.mTheme = theme;
            return this;
        }

        public Builder setLayoutId(@LayoutRes int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        public Builder setIsFullScreen(boolean isFullScreen) {
            this.isFullScreen = isFullScreen;
            return this;
        }

        public Builder setGravity(int gravity) {
            mGravity = gravity;
            return this;
        }

        public Builder setRelativeWidth(@FloatRange(from = 0.0f, to = 1.0f)float relativeWidth){
            mRelativeWidth = relativeWidth;
            return this;
        }

        public Builder setRelativeHeight(@FloatRange(from = 0.0f, to = 1.0f)float relativeHeight){
            mRelativeHeight = relativeHeight;
            return this;
        }

        public Builder setBgColor(@ColorInt int color) {
            this.mBgColor = color;
            return this;
        }

    }


}

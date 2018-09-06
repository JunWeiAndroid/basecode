package com.gemo.common.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.gemo.common.base.BaseActivity;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;


/**
 * 在 {@link Dialog} 的基础上重新定制了 {@link #show()} 和 {@link #hide()} 时的动画效果, 使 {@link Dialog} 在界面底部升起和降下。
 */
public class TopOrBottomDialog extends Dialog {
    private Context context;

    public enum ShowDialogLocation{
        TOP, BOTTOM
    }

    private ShowDialogLocation showLocation;

    private static final String TAG = "QMUIBottomSheet";
    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画
    private View mContentView;
    private boolean mIsAnimating = false;

    private OnDialogShowListener mOnBottomSheetShowListener;

    public TopOrBottomDialog(Context context, ShowDialogLocation showDialogLocation) {
        super(context, com.qmuiteam.qmui.R.style.QMUI_BottomSheet);
        this.showLocation = showDialogLocation;
        this.context = context;
    }

    public void setOnBottomSheetShowListener(OnDialogShowListener onBottomSheetShowListener) {
        mOnBottomSheetShowListener = onBottomSheetShowListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (showLocation == ShowDialogLocation.TOP) {
            params.gravity = Gravity.TOP | Gravity.CENTER;
        }else {
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        }

        int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext());
        int screenHeight = QMUIDisplayHelper.getScreenHeight(getContext());
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate;
        AlphaAnimation alpha;
        if (showLocation == ShowDialogLocation.TOP) {
            translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f
            );
            alpha = new AlphaAnimation(1, 0);
        } else {
            translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
            );
            alpha = new AlphaAnimation(0, 1);
        }
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        if (showLocation == ShowDialogLocation.TOP){
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsAnimating = false;
                    /**
                     * Bugfix： Attempting to destroy the window while drawing!
                     */
                    mContentView.post(new Runnable() {
                        @Override
                        public void run() {
                            // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                            // 在dismiss的时候可能已经detach了，简单try-catch一下
                            try {
                                TopOrBottomDialog.super.dismiss();
                            } catch (Exception e) {
                                QMUILog.w(TAG, "dismiss error\n" + Log.getStackTraceString(e));
                            }
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate;
        AlphaAnimation alpha;
        if (showLocation == ShowDialogLocation.TOP) {
            translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f
            );
            alpha = new AlphaAnimation(0, 1);
        }else {
            translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
            );
            alpha = new AlphaAnimation(1, 0);
        }
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        if (showLocation == ShowDialogLocation.BOTTOM){
            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mIsAnimating = false;
                    /**
                     * Bugfix： Attempting to destroy the window while drawing!
                     */
                    mContentView.post(new Runnable() {
                        @Override
                        public void run() {
                            // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                            // 在dismiss的时候可能已经detach了，简单try-catch一下
                            try {
                                TopOrBottomDialog.super.dismiss();
                            } catch (Exception e) {
                                QMUILog.w(TAG, "dismiss error\n" + Log.getStackTraceString(e));
                            }
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        mContentView.startAnimation(set);
    }

    @Override
    public void show() {
        BaseActivity act = (BaseActivity) context;
        if (act == null || act.isDestroyed()){
            return;
        }
        super.show();
        if (showLocation == ShowDialogLocation.TOP) {
            animateDown();
        }else {
            animateUp();
        }
        if (mOnBottomSheetShowListener != null) {
            mOnBottomSheetShowListener.onShow();
        }
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        if (showLocation == ShowDialogLocation.TOP) {
            animateUp();
        }else {
            animateDown();
        }
    }

    public interface OnDialogShowListener {
        void onShow();
    }

}


package com.gemo.common.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.gemo.common.R;
import com.gemo.common.base.BaseDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

/**
 * Dialog相关
 * Created by WJW on 2018/5/10.
 */

public class DialogUtils {

    public static final int mDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    /**
     * 不显示任何icon
     */
    public static final int TYPE_NOTHING = QMUITipDialog.Builder.ICON_TYPE_NOTHING;
    /**
     * 显示 Loading 图标
     */
    public static final int TYPE_LOADING = QMUITipDialog.Builder.ICON_TYPE_LOADING;
    /**
     * 显示成功图标
     */
    public static final int TYPE_SUCCESS = QMUITipDialog.Builder.ICON_TYPE_SUCCESS;
    /**
     * 显示失败图标
     */
    public static final int TYPE_FALL = QMUITipDialog.Builder.ICON_TYPE_FAIL;
    /**
     * 显示信息图标
     */
    public static final int TYPE_INFO = QMUITipDialog.Builder.ICON_TYPE_INFO;

    /**
     * 确认类型对话框
     *
     * @param context
     * @param title              标题
     * @param msg                content
     * @param cancelBtnText      取消按钮文字
     * @param cancelBtnListener  取消按钮监听
     * @param confirmBtnText     确认按钮文字
     * @param confirmBtnListener 确认按钮监听
     */
    public static void showConfirmDialog(Context context, String title, String msg,
                                         String cancelBtnText, DialogInterface.OnClickListener cancelBtnListener,
                                         String confirmBtnText, DialogInterface.OnClickListener confirmBtnListener) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msg)
                .addAction(cancelBtnText, (dialog, index) -> {
                    if (cancelBtnListener != null) {
                        cancelBtnListener.onClick(dialog, index);
                    }
                    dialog.dismiss();
                })
                .addAction(confirmBtnText, (dialog, index) -> {
                    if (confirmBtnListener != null) {
                        confirmBtnListener.onClick(dialog, index);
                    }
                    dialog.dismiss();
                })
                .create(mDialogStyle).show();
    }


    /**
     * 输入框Dialog
     *
     * @param context         context
     * @param title           标题
     * @param hint            et Hint
     * @param cancelText      取消按钮文本
     * @param confirmText     确认按钮文本
     * @param confirmListener 确认按钮监听
     */
    public static void showEditTextDialog(Context context, String title, String hint, String cancelText, String confirmText, OnEditTextDialogClickConfirmListener confirmListener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setCancelable(true)
                .setPlaceholder(hint)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(cancelText, (dialog, index) -> dialog.dismiss())
                .addAction(confirmText, (dialog, index) -> {
                    CharSequence text = builder.getEditText().getText();
                    if (confirmListener != null) {
                        confirmListener.onConfirm(dialog, text.toString());
                    }
                })
                .create(mDialogStyle).show();
    }

    public interface OnEditTextDialogClickConfirmListener {
        void onConfirm(QMUIDialog dialog, String editTextContent);
    }

    /**
     * 单选类型Dialog
     *
     * @param context                context
     * @param items                  选项数组
     * @param defaultIndex 默认选中项，如果没有，传入-1或者大于items.length都可以
     * @param onSingleChoiceListener 选择监听
     */
    public static void showSingleChoiceDialog(Context context, String[] items, int defaultIndex, OnSingleChoiceListener onSingleChoiceListener) {
        new QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(defaultIndex)
                .addItems(items, (dialog, which) -> {
                    if (onSingleChoiceListener != null) {
                        onSingleChoiceListener.onChoice(which, items[which]);
                    }
                    dialog.dismiss();
                })
                .create(mDialogStyle).show();
    }

    public interface OnSingleChoiceListener {
        void onChoice(int position, String text);
    }

    /**
     * Create menu list dialog qmui dialog.
     * 列表类菜单选择单项
     *
     * @param context  the context
     * @param items    菜单项
     * @param listener 选中事件
     */
    public static void showMenuListDialog(Context context, String[] items, OnMenuSelectListener listener) {
        new QMUIDialog.MenuDialogBuilder(context)
                .addItems(items, (dialog, which) -> {
                    if (listener != null) {
                        listener.onSelect(which, items[which]);
                    }
                    dialog.dismiss();
                })
                .create(mDialogStyle).show();
    }

    public interface OnMenuSelectListener {
        void onSelect(int position, String text);
    }

    /**
     * Show message dialog.
     * 只显示提示信息，没有操作
     * @param context the context
     * @param title   the title
     * @param message the message
     */
    public static void showMessageDialog(Context context, String title, CharSequence message) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .create(mDialogStyle).show();
    }

    /**
     * Create loading dialog dialog.
     *
     * @param context the context
     * @return the dialog
     */
    public static Dialog createLoadingDialog(Context context) {
        return new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(context.getString(R.string.loading) )
                .create();
    }

    /**
     * Create tips dialog dialog.
     *
     * @param context the context
     * @param type    the orderStatus
     * @param tips    the tips
     * @return the dialog
     */
    public static Dialog createTipsDialog(Context context,@Type int type, CharSequence tips) {
        return new QMUITipDialog.Builder(context)
                .setIconType(type)
                .setTipWord(tips)
                .create();
    }

    @IntDef({TYPE_NOTHING, TYPE_LOADING, TYPE_SUCCESS, TYPE_FALL, TYPE_INFO})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type{}

}

package com.gemo.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

/**
 * Created by WJW
 * 使用DataBinding的BaseViewHolder
 */

public class DataBindVH extends BaseViewHolder {
    public ViewDataBinding mBinding;

    public DataBindVH(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    @SuppressWarnings("unchecked")
    public final <T extends ViewDataBinding> T getBinding() {
        return (T) mBinding;
    }
}

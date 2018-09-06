package com.gemo.common.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by WJW
 * BaseAdapter 的ViewHolder
 */

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;

    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = findView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}

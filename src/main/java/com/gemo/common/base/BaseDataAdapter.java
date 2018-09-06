package com.gemo.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by WJW
 * 邮箱:
 * 使用DataBinding 的基础Adapter
 */

public abstract class BaseDataAdapter<T> extends BaseAdapter<T, DataBindVH> {

    public BaseDataAdapter(List<T> mData, Context mContext) {
        super(mData, mContext);
    }

    @Override
    protected DataBindVH createViewHolder(View view, int viewType) {
        return new DataBindVH(view);
    }

    /**
     * 通过改变View的布局宽高达到显示/隐藏View的效果
     *
     * @param view    itemView
     * @param visible 是否可见
     */
    protected void changeViewVisible(View view, boolean visible) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.height = visible ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        params.width = visible ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
        view.setLayoutParams(params);
    }


}

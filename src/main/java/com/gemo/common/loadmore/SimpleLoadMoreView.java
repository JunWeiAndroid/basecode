package com.gemo.common.loadmore;


import com.gemo.common.R;

/**
 * Created by BlingBling on 2016/10/11.
 * Add by hzg on 2017/12/12.
 * LoadMoreView 的一个简单的示例，
 * BaseAdapter中所有自定义LoadMoreView 类似这样实现
 */

public final class SimpleLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}

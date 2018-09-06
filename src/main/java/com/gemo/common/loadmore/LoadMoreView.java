package com.gemo.common.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.gemo.common.base.BaseViewHolder;


/**
 * Created by BlingBling on 2016/11/11.
 * Add by hzg on 2017/12/12.
 * BaseAdapter中的加载View抽象类，所有加载VIew都需要继承它
 */

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void convert(BaseViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;
            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            default:
        }
    }

    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadingViewId(), visible);
    }

    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisible(getLoadFailViewId(), visible);
    }

    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        final int loadEndViewId = getLoadEndViewId();
        if (loadEndViewId != 0) {
            holder.setVisible(loadEndViewId, visible);
        }
    }

    public final void setLoadMoreEndGone(boolean loadMoreEndGone) {
        this.mLoadMoreEndGone = loadMoreEndGone;
    }

    public final boolean isLoadEndMoreGone() {
        return getLoadEndViewId() == 0 || mLoadMoreEndGone;
    }


    /**
     * load more layout
     *
     * @return load More View 布局资源Id
     */
    @LayoutRes
    public abstract int getLayoutId();

    /**
     * loading view
     *
     * @return load More View 布局中 加载中View 的id
     */
    @IdRes
    protected abstract int getLoadingViewId();

    /**
     * load fail view
     *
     * @return load More View 布局中 加载失败View 的id
     */
    @IdRes
    protected abstract int getLoadFailViewId();

    /**
     * load end view, you can return 0
     *
     * @return load More View 布局中 加载完成View 的id
     */
    @IdRes
    protected abstract int getLoadEndViewId();
}

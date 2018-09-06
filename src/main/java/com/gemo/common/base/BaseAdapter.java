package com.gemo.common.base;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemo.common.loadmore.LoadMoreView;
import com.gemo.common.loadmore.OnScrollLoadListener;
import com.gemo.common.loadmore.SimpleLoadMoreView;
import com.gemo.common.util.Logger;

import java.util.List;

/**
 * Created by WJW
 * 基础Adapter
 */

@SuppressWarnings({"unused", "SameParameterValue", "WeakerAccess"})
abstract public class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> mData;
    protected OnClickListener<T> mListener;
    protected Context mContext;
    private int LOADING_VIEW = 0x034637;


    //load more
    private boolean mNextLoadEnable = true;//是否还有数据可以加载
    private boolean mLoadMoreEnable = false;//是否可以加载更多
    private boolean mLoading = false;
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    private LoadMoreListener mLoadMoreListener;
    private OnScrollLoadListener mOnScrollLoadListener;
    private RecyclerView mRecyclerView;

    private boolean useLoadMoreInOnBindView = false;//在 OnBindViewHolder 使用loadore

    public BaseAdapter(List<T> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOADING_VIEW) {
            View view = LayoutInflater.from(mContext).inflate(mLoadMoreView.getLayoutId(), parent, false);
            return createViewHolder(view, LOADING_VIEW);
        }
        View view = LayoutInflater.from(mContext).inflate(getViewLayoutResId(viewType), parent, false);
        return createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (useLoadMoreInOnBindView) {
            autoLoadMore(position);
        }

        if (holder.getItemViewType() == LOADING_VIEW) {
            mLoadMoreView.convert(holder);
            holder.itemView.setOnClickListener(view -> {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
//                    ToastUtil.toastS("加载失败,点我重试");
                    loadMoreStart();
                    mLoadMoreListener.loadMore(BaseAdapter.this);
                }
            });
        } else {
            covert(holder, getItem(position), position);
        }

    }

    /**
     * @return mData List的数量
     */
    public int getDataSize() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * 除了加载loading View 之外的View的数量
     *
     * @return View的数量
     */
    protected int getViewSize() {
        return getDataSize();
    }

    @Override
    public int getItemCount() {
        return getViewSize() + getLoadMoreViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mLoadMoreEnable && position >= getViewSize()) {
            return LOADING_VIEW;
        } else {
            return getDefItemViewType(position);
        }

    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 获取mData中下标为position的数据
     *
     * @param position 位置下标
     * @return mData中下标为position的数据
     */
    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        if (position < 0) {
            return null;
        }
        if (position < mData.size()) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    /**
     * 设置新的数据,并刷新adapter
     *
     * @param data 新的数据
     */
    public void setNewData(@Nullable List<? extends T> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * @param data 加入多条数据
     */
    public void addAllData(@NonNull List<? extends T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Add all to head.
     * 加到顶部
     *
     * @param data the data
     */
    public void addAllToHead(List<T> data) {
        if (data == null) {
            return;
        }
        mData.addAll(0, data);
        notifyItemRangeChanged(data.size(), mData.size());
    }

    /**
     * Add data.
     * 加到指定位置
     *
     * @param position the position
     * @param data     the data
     */
    public void addData(int position, T data) {
        mData.add(position, data);
        notifyItemRangeChanged(1, mData.size());
    }

    /**
     * @param data 加入单条数据
     */
    public void addData(@NonNull T data) {
        this.mData.add(data);
        notifyDataSetChanged();
    }

    /**
     * 移除一个item
     *
     * @param data 被移除的item
     */
    public void remove(@NonNull T data) {
        this.mData.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 移除一个item
     *
     * @param position 被移除item的position
     */
    public void remove(int position) {
        this.mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getDataSize() - position);
    }

    protected abstract void covert(VH holder, T item, int position);

    @LayoutRes
    protected abstract int getViewLayoutResId(int viewType);

    abstract protected VH createViewHolder(View view, int viewType);

    public List<T> getData() {
        return mData;
    }

    /**
     * 使用OnScrollListener 实现加载更多
     * 设置loadMore 监听
     *
     * @param mLoadMoreListener loadMore 监听
     * @param recyclerView      需要传入绑定的recyclerView,利用它的post方法刷新Adapter,避免crash
     */
    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener, RecyclerView recyclerView) {
        this.mLoadMoreListener = mLoadMoreListener;
        this.mRecyclerView = recyclerView;
        if (mOnScrollLoadListener == null) {
            mOnScrollLoadListener = new OnScrollLoadListener() {
                @Override
                public void onLoadMore() {
                    Logger.d("到达底部");
                    BaseAdapter.this.onLoadMore();
                }
            };
            recyclerView.addOnScrollListener(mOnScrollLoadListener);
        }
    }

    public void onLoadMore() {
        if (!mLoading && mLoadMoreEnable) {
            if (mRecyclerView != null) {
                mRecyclerView.post(() -> {
                    if (mNextLoadEnable) {
                        loadMoreStart();
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.loadMore(BaseAdapter.this);
                        }
                    }
                });
            }

        }
    }

    /**
     * 在onBindViewHolder 实现加载更多
     * 设置loadMore 监听
     * 滑到底部自动加载
     *
     * @param mLoadMoreListener
     * @param recyclerView
     */
    public void setAutoLoadMoreListener(LoadMoreListener mLoadMoreListener, RecyclerView recyclerView) {
        setEnableLoadMore(true);
        setUseLoadMoreInOnBindView(true);
        this.mLoadMoreListener = mLoadMoreListener;
        this.mRecyclerView = recyclerView;
    }

    /**
     * 获取Loading View的个数
     *
     * @return 0或1, 0表示不显示loading view,1显示
     */
    protected int getLoadMoreViewCount() {
        if (!mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }

        if (mData.size() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Gets to load more locations
     *
     * @return LoadingView 的位置下标
     */
    protected int getLoadMoreViewPosition() {
        return mData.size();
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoading;
    }

    /**
     * Refresh startActivity, no more data
     */
    public void loadMoreStart() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    @SuppressWarnings("WeakerAccess")
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(@SuppressWarnings("SameParameterValue") boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    /**
     * Returns the enabled status for load more.
     *
     * @return True if load more is enabled, false otherwise.
     */
    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }


    public void setOnClickListener(OnClickListener<T> mListener) {
        this.mListener = mListener;
    }


    private void autoLoadMore(int position) {
        Logger.d(position);
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - 1) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING || mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
            Logger.d("return" + position);
            return;
        }

        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            if (getRecyclerView() != null) {
                getRecyclerView().post(new Runnable() {
                    @Override
                    public void run() {
                        Logger.d("LoadMore:" + position);
                        mLoadMoreListener.loadMore(BaseAdapter.this);
                    }
                });
            } else {
                Logger.d("LoadMore:" + position);
                mLoadMoreListener.loadMore(BaseAdapter.this);
            }
        }
    }

    public void setUseLoadMoreInOnBindView(boolean useLoadMoreInOnBindView) {
        this.useLoadMoreInOnBindView = useLoadMoreInOnBindView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * LoadMore 监听
     */
    public interface LoadMoreListener {
        void loadMore(BaseAdapter adapter);
    }

    /**
     * 点击监听
     *
     * @param <T>
     */
    public interface OnClickListener<T> {
        void onClick(int position, T item);
    }
}

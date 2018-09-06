package com.gemo.common.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by DELL on 2017年11月24日 024.
 * Add by hzg on 2017/12/12
 * 上拉加载更多
 */

public abstract class OnScrollLoadListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //dy>0:上拉
        if ((dy > 0 || dx > 0) && visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1) {
            onLoadMore();
        }
    }

    /**
     * 加载更多
     * 这个只是表示到达底部需要执行的操作，其他是否可以加载或者加载状态不在这里判断，需要自己判断
     */
    public abstract void onLoadMore();

}

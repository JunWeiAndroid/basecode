package com.gemo.common.base;

import com.gemo.common.util.Logger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by WJW
 * BasePresenter: MVP P层基类
 */

public abstract class BasePresenter<V extends BaseView> {

    protected V mView;

    private CompositeDisposable mDisposables;

    /**
     * 绑定view
     *
     * @param view BaseView 子类
     */
    public void attach(V view) {
        this.mView = view;
        mDisposables = new CompositeDisposable();
    }

    public CompositeDisposable getDisposables() {
        return mDisposables;
    }

    /**
     * 取消订阅
     */
    public void unSubscribe() {
        mDisposables.clear();
        mView = null;
    }

    /**
     * 添加disposable到CompositeDisposable
     *
     * @param d disposable
     */
    protected boolean addDisposable(Disposable d) {
        return mDisposables.add(d);
    }

    /*
        protected boolean removeDisposable(Disposable disposable) {
            return mDisposables.remove(disposable);
        }
        */
    protected boolean addAllDisposable(Disposable... disposables) {
        return mDisposables.addAll(disposables);
    }

    /**
     * 移除订阅，相当于取消一次订阅或者理解为让此次监听失效
     *
     * @param d 需要取消的订阅
     */
    protected boolean dispose(Disposable d) {
        return d == null || mDisposables.remove(d);
    }

    /**
     * Is null or dispose boolean.
     * 判断一个disposable是否为空或者被处理
     *
     * @param disposable the disposable
     * @return the boolean
     */
    protected boolean isNullOrDisposed(Disposable disposable) {
        Logger.i("disposable=" + (disposable == null ? "null" : disposable.isDisposed()));
        return disposable == null || disposable.isDisposed();
    }

    /**
     * Disposable 不为空且没有disposed（一个任务还未执行完）
     */
    protected boolean isNotNullOrDisposed(Disposable disposable) {
        Logger.i("disposable=" + (disposable == null ? "null" : disposable.isDisposed()));
        return disposable != null && !disposable.isDisposed();
    }

    /**
     * Gets data model.
     *
     * @param <M>   the orderStatus parameter
     * @param clazz the clazz
     * @return the model
     */
    protected <M extends IBaseData> M getModel(Class<M> clazz) {
        return DataManager.getInstance().create(clazz);
    }

}

package com.gemo.common.util;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by DELL on 2018年1月3日 003.
 * E-Mail:n.zjx@163.com
 * RxUtil: Rx工具类
 */

public class RxUtil {

    private static RxUtil sRx;

    private final Subject<Object> BUS;

    private RxUtil() {
        BUS = PublishSubject.create().toSerialized();
    }

    /**
     * IO 线程切换到主线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> io2Main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static RxUtil getDefault() {
        if (sRx == null) {
            sRx = Holder.HOLDER;
        }
        return sRx;
    }

    /**
     * 根据传入的类型得到一个消息发布者
     *
     * @param type 发送的数据类型
     * @param <T>  发送的数据类型
     * @return 被观察者（消息发布者）
     */
    public <T> Flowable<T> toObservable(Class<T> type) {
        return BUS.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    /**
     * 根据传入的类型得到一个消息发布者，返回数据在主线程执行
     *
     * @param type 发送的数据类型
     * @param <T>  发送的数据类型
     * @return 被观察者（消息发布者）
     */
    public <T> Flowable<T> toObservableOnMain(Class<T> type) {
        return BUS.toFlowable(BackpressureStrategy.BUFFER).ofType(type).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送数据
     *
     * @param o postData
     */
    public void post(Object o) {
        BUS.onNext(o);
    }

    private static class Holder {
        static final RxUtil HOLDER = new RxUtil();
    }

}

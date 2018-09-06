package com.gemo.common.util;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by DELL on 2018年6月15日 015.
 * E-Mail:n.zjx@163.com
 * Android
 * RxView: 使用RxJava 处理View 相关事件, 例如重复点击
 */
public class RxView {

    /**
     * @param view clickView
     */
    public static Observable<View> clicks(@NonNull View view) {
        return new ViewClickObservable(view);
    }

    /**
     * 1 秒内无法重复点击
     * @param view clickView
     * @param consumer 事件处理
     */
    public static Disposable clicks(@NonNull View view, Consumer<View> consumer) {
        return new ViewClickObservable(view).throttleFirst(1, TimeUnit.SECONDS).subscribe(consumer);
    }

    private static class ViewClickObservable extends Observable<View> {

        private View view;

        ViewClickObservable(View view) {
            this.view = view;
        }

        @Override
        protected void subscribeActual(Observer<? super View> observer) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                return;
            }
            Listener listener = new Listener(view, observer);
            observer.onSubscribe(listener);
            view.setOnClickListener(listener);
        }
    }

    private static class Listener extends MainThreadDisposable implements View.OnClickListener {

        private View view;
        private Observer<? super View> observer;

        public Listener(View view, Observer<? super View> s) {
            this.view = view;
            this.observer = s;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                observer.onNext(v);
            }
        }

        @Override
        protected void onDispose() {
            view.setOnClickListener(null);
        }
    }
}

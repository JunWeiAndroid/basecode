package com.gemo.common.net;

import android.support.annotation.NonNull;

import com.gemo.common.R;
import com.gemo.common.util.NeedReLogin;
import com.gemo.common.util.RxUtil;
import com.gemo.common.util.ToastUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Wjw
 * HttpResultSubscriber: Http结果订阅者
 * update: Dell 2018-5-14 n.zjx@163.com
 * Result泛型
 */

public abstract class HttpResultSubscriber<T> extends DisposableSubscriber<Result<T>> {

    @Override
    public void onNext(Result<T> t) {
        switch (t.code) {
            case 200:
                onSuccess(t.data);
                break;
            case 302: {// 需要重新登录
                RxUtil.getDefault().post(new NeedReLogin());
                HttpResultException e = new HttpResultException(t.code, t.msg);
                toast(e.getMsg());
                onFailure(HttpError.NEED_RELOGIN, e);
                e.printStackTrace();
                break;
            }
            default: {
                HttpResultException e = new HttpResultException(t.code, t.msg);
                toast(e.getMsg());
                onFailure(HttpError.RESULT_ERROR, e);
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        cancel();
        HttpError error = HttpError.ERROR;
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            toast(R.string.connect_failed);
            error = HttpError.CONNECT_ERROR;
        } else if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
            toast(R.string.connect_timeout);
            error = HttpError.TIMEOUT;
        } else if (t instanceof JSONException || t instanceof JsonIOException
                || t instanceof JsonSyntaxException || t instanceof MalformedJsonException) {
            toast(R.string.data_parse_error);
            error = HttpError.DATA_PARSE_ERROR;
        } else {
            toast(t.getMessage());
        }
        onFailure(error, (Exception) t);
        t.printStackTrace();
//        BuglyLog.w(("HTTP_ERROR:" + error.toString()).intern(), t.toString());//上传打印日志到bugly
    }

    @Override
    public void onComplete() {
        cancel();
    }

    private void toast(String msg) {
        ToastUtil.toastS(msg);
    }

    private void toast(int msg) {
        ToastUtil.toastS(msg);
    }

    /**
     * 请求成功回调
     *
     * @param result 回调数据
     */
    public abstract void onSuccess(T result);

    /**
     * 请求失败回调
     *
     * @param error
     * @param e     失败异常信息
     */
    public abstract void onFailure(HttpError error, @NonNull Exception e);

}

package me.xtree.mvvmhabit.utils;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.ExceptionHandle;

/**
 * Created by goldze on 2017/6/19.
 * 有关Rx的工具类
 */
public class RxUtils {
    /**
     * 生命周期绑定
     *
     * @param lifecycle Activity
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull Context lifecycle) {
        if (lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("context not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Fragment
     */
    public static LifecycleTransformer bindToLifecycle(@NonNull Fragment lifecycle) {
        if (lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("fragment not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Fragment
     */
    public static LifecycleTransformer bindToLifecycle(@NonNull LifecycleProvider lifecycle) {
        return lifecycle.bindToLifecycle();
    }

    /**
     * 线程调度器
     */
    /*public static ObservableTransformer schedulersTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }*/

    public static FlowableTransformer schedulersTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /*public static ObservableTransformer exceptionTransformer() {
        return new ErrorTransformer();
    }*/

    private static class ErrorTransformer<T> implements FlowableTransformer {
        @Override
        public Publisher apply(Flowable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Flowable<T>) upstream
                    //.map(new HandleFuc<T>())
                    .onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }

    public static FlowableTransformer exceptionTransformer() {
        return new ErrorTransformer();
    }

    /*private static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Observable<T>) upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }*/

    /*private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }*/

    private static class HttpResponseFunc<T> implements Function<Throwable, Flowable<T>> {
        @Override
        public Flowable<T> apply(Throwable t) {
            return Flowable.error(ExceptionHandle.handleException(t));
        }
    }

    private static class HandleFuc<T> implements Function<BaseResponse<T>, T> {
        @Override
        public T apply(BaseResponse<T> response) {
            if (!response.isOk())
                throw new RuntimeException(!"".equals(response.getStatus() + "" + response.getMessage()) ? response.getMessage() : "");
            return response.getData();
        }
    }

}

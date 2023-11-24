package me.xtree.mvvmhabit.http;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.LambdaSubscriber;

public class ApiCallBack<T> {
    private ApiSubscriber<T> mApiSubscriber;
    private Flowable<T> mFlowable;

    public ApiCallBack(ApiSubscriber apiCallBack, Flowable flowable){
        mApiSubscriber = apiCallBack;
        mFlowable = flowable;
    }

    public Consumer<? super T> onNext(){
        return (Consumer<T>) o -> mApiSubscriber.onNext(o);
    }

    public Consumer<? super Throwable> onError(){
        return (Consumer<Throwable>) t -> mApiSubscriber.onError(t);
    }

    public Consumer<? super Subscription> onSubscribe(){
        return (Consumer<Subscription>) subscription -> mApiSubscriber.onSubscribe(subscription);
    }

    public Action onComplete(){
        return () -> mApiSubscriber.onComplete();
    }

    public Disposable getDisposable(ApiCallBack<T> apiBack){
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(apiBack.onNext(), apiBack.onError(), apiBack.onComplete(), apiBack.onSubscribe());
        mFlowable.subscribe(ls);
        return  ls;
    }
}

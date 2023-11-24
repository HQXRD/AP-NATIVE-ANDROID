package me.xtree.mvvmhabit.http;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.LambdaSubscriber;

public class ApiBack<T> {
    private ApiCallBack<T> mApiCallBack;
    private Flowable<T> mFlowable;

    public ApiBack(ApiCallBack apiCallBack, Flowable flowable){
        mApiCallBack = apiCallBack;
        mFlowable = flowable;
    }

    public Consumer<? super T> onNext(){
        return (Consumer<T>) o -> mApiCallBack.onNext(o);
    }

    public Consumer<? super Throwable> onError(){
        return (Consumer<Throwable>) t -> mApiCallBack.onError(t);
    }

    public Consumer<? super Subscription> onSubscribe(){
        return (Consumer<Subscription>) subscription -> mApiCallBack.onSubscribe(subscription);
    }

    public Action onComplete(){
        return () -> mApiCallBack.onComplete();
    }

    public Disposable getDisposable(ApiBack<T> apiBack){
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(apiBack.onNext(), apiBack.onError(), apiBack.onComplete(), apiBack.onSubscribe());

        mFlowable.subscribe(ls);
        return  ls;
    }
}

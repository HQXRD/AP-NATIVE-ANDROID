package com.xtree.home.data.source;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    //模拟登录
    io.reactivex.rxjava3.core.@NonNull Observable<Object> login();

    io.reactivex.rxjava3.core.Observable<BaseResponse<Object>> demoGet();

    Observable<BaseResponse<Object>> demoPost(String catalog);


}

package com.xtree.mine.data.source;


import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    //模拟登录
    @NonNull Observable<Object> login();

    Observable<BaseResponse<Object>> demoGet();

    Observable<BaseResponse<Object>> demoPost(String catalog);

    HttpApiService getApiService();

    Flowable<GameRebateAgrtResponse> getGameRebateAgrtData(String url, GameRebateAgrtRequest request);

    Flowable<GameSubordinateAgrteResponse> getGameSubordinateAgrteData(String url, GameSubordinateAgrteRequest request);

    Flowable<GameSubordinateRebateResponse> getGameSubordinateRebateData(String url, GameSubordinateRebateRequest request);
}

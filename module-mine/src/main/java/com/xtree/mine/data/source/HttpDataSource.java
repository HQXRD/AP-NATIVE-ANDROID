package com.xtree.mine.data.source;


import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.DividendAutoSentQuery;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.request.RebateAgrtCreateRequest;
import com.xtree.mine.vo.request.RebateAgrtCreateQuery;
import com.xtree.mine.vo.request.RecommendedReportsRequest;
import com.xtree.mine.vo.response.DividendAgrtCheckResponse;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;
import com.xtree.mine.vo.response.RebateAgrtCreateResponse;
import com.xtree.mine.vo.response.RecommendedReportsResponse;

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

    /**
     * 返水契约-游戏场馆-**返水
     */
    Flowable<GameRebateAgrtResponse> getGameRebateAgrtData(String url, GameRebateAgrtRequest request);

    /**
     * 返水契约-游戏场馆-下级契约
     */
    Flowable<GameSubordinateAgrteResponse> getGameSubordinateAgrteData(String url, GameSubordinateAgrteRequest request);

    /**
     * 返水契约-游戏场馆-下级返水
     */
    Flowable<GameSubordinateRebateResponse> getGameSubordinateRebateData(String url, GameSubordinateRebateRequest request);

    /**
     * 返水契约-分红契约
     */
    Flowable<GameDividendAgrtResponse> getGameDividendAgrtData(GameDividendAgrtRequest request);

    /**
     * 返水契约-分红契约-一键发放
     */
    Flowable<DividendAutoSendResponse> getDividendAutoSendData(DividendAutoSentQuery query, DividendAutoSendRequest request);

    /**
     * 返水契约-推荐报表
     */
    Flowable<RecommendedReportsResponse> getRecommendedReportsData(RecommendedReportsRequest request);

    /**
     * 返水契约-创建契约
     */
    Flowable<RebateAgrtCreateResponse> getRebateAgrtCreateData(RebateAgrtCreateQuery query, RebateAgrtCreateRequest request);

    /**
     * 返水契约-分红契约规则详情
     */
    Flowable<DividendAgrtCheckResponse> getDividendAgrtData(DividendAgrtCheckRequest request);

}

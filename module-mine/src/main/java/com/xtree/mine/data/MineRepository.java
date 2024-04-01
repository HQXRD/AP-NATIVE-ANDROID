package com.xtree.mine.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.LocalDataSource;
import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.CommissionsReportsRequest;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.DividendAgrtSendQuery;
import com.xtree.mine.vo.request.DividendAgrtSendRequest;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.DividendAutoSentQuery;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.request.RebateAgrtCreateQuery;
import com.xtree.mine.vo.request.RebateAgrtCreateRequest;
import com.xtree.mine.vo.request.RecommendedReportsRequest;
import com.xtree.mine.vo.response.DividendAgrtCheckResponse;
import com.xtree.mine.vo.response.DividendAgrtSendReeponse;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;
import com.xtree.mine.vo.response.RebateAgrtCreateResponse;
import com.xtree.mine.vo.response.RecommendedReportsResponse;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class MineRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static MineRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private MineRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static MineRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (MineRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MineRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public @NonNull Observable<Object> login() {
        return mHttpDataSource.login();
    }

    @Override
    public Observable<BaseResponse<Object>> demoGet() {
        return null;
    }

    @Override
    public Observable<BaseResponse<Object>> demoPost(String catalog) {
        return null;
    }

    @Override
    public HttpApiService getApiService() {
        return mHttpDataSource.getApiService();
    }

    @Override
    public Flowable<GameRebateAgrtResponse> getGameRebateAgrtData(String url, GameRebateAgrtRequest request) {
        return mHttpDataSource.getGameRebateAgrtData(url, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<GameSubordinateAgrteResponse> getGameSubordinateAgrteData(String url, GameSubordinateAgrteRequest request) {
        return mHttpDataSource.getGameSubordinateAgrteData(url, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<GameSubordinateRebateResponse> getGameSubordinateRebateData(String url, GameSubordinateRebateRequest request) {
        return mHttpDataSource.getGameSubordinateRebateData(url, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<GameDividendAgrtResponse> getGameDividendAgrtData(GameDividendAgrtRequest request) {
        return mHttpDataSource.getGameDividendAgrtData(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<DividendAutoSendResponse> getDividendAutoSendData(DividendAutoSentQuery query, DividendAutoSendRequest request) {
        return mHttpDataSource.getDividendAutoSendData(query, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<RecommendedReportsResponse> getRecommendedReportsData(RecommendedReportsRequest request) {
        return mHttpDataSource.getRecommendedReportsData(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<RebateAgrtCreateResponse> getRebateAgrtCreateData(RebateAgrtCreateQuery query, RebateAgrtCreateRequest request) {
        return mHttpDataSource.getRebateAgrtCreateData(query, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<DividendAgrtCheckResponse> getDividendAgrtData(DividendAgrtCheckRequest request) {
        return mHttpDataSource.getDividendAgrtData(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<DividendAgrtSendReeponse> getDividendAgrtSendStep1Data(DividendAgrtSendQuery query, DividendAgrtSendRequest request) {
        return mHttpDataSource.getDividendAgrtSendStep1Data(query,request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable<DividendAgrtSendReeponse> getDividendAgrtSendStep2Data(DividendAgrtSendQuery query,DividendAgrtSendRequest request) {
        return mHttpDataSource.getDividendAgrtSendStep2Data(query, request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable getFunctionMenuData() {
        return mHttpDataSource.getFunctionMenuData()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }

    @Override
    public Flowable getCommissionsData(CommissionsReportsRequest request) {
        return mHttpDataSource.getCommissionsData(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer());
    }


    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }


}

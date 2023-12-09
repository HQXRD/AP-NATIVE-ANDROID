package com.xtree.mine.data.source.http.service;

import com.xtree.base.vo.FBService;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.SettingsVo;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by goldze on 2017/6/15.
 */

public interface HttpApiService {
    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<Object>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<Object>> demoPost(@Field("catalog") String catalog);

    @POST("/api/auth/login")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<LoginResultVo>> login(@Body Map<String, String> map);

    @POST("/api/register/kygprka")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> register(@Body Map<String, String> map);

    @GET("/api/settings/?")
    Flowable<BaseResponse<SettingsVo>> getSettings(@QueryMap Map<String, String> filters);

    /**
     *获取 平台中心余额
     * @return
     */
    @GET("/api/account/balance")
    Flowable<BaseResponse<BalanceVo>> getBalance();

    /**
     * 获取 某个场馆的余额 <p/>
     * eg: "balance": "3.5000"
     */
    @GET("/api/game/{gameAlias}/balance")
    Flowable<BaseResponse<GameBalanceVo>> getGameBalance(@Path("gameAlias") String gameAlias);

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> getPMGameTokenApi();
}

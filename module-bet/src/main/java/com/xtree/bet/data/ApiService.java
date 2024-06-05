package com.xtree.bet.data;

import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.response.HotLeagueInfo;

import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {
    /**
     * 热门联赛ID列表
     *
     * @return
     */
    @GET("/api/settings/?")
    Flowable<BaseResponse<HotLeagueInfo>> getSettings(@QueryMap Map<String, String> filters);

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB杏彩体育请求服务地址
     * @return
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();

    /**
     * 获取 PM杏彩体育2请求服务地址
     * @return
     */
    @POST("/api/sports/obgzy/getToken?cachedToken=0")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMXCGameTokenApi();

    /**
     * 异常日志上报
     * @return
     */
    @POST("/api/sports/excaption")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> uploadExcetion(@Body Map<String, String> map);

    /**
     * 获取场馆代理开关
     */
    @POST("/api/sports/gsaswitch")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Map<String, String>>> getGameSwitch();

    /**
     * 获取 平台中心余额
     */
    @GET("/api/account/balance")
    Flowable<BaseResponse<BalanceVo>> getBalance();

    /**
     * 获取PM/FB游戏的链接
     */
    @GET("/api/game/{gameAlias}/playurl")
    Flowable<BaseResponse<Map<String, Object>>> getPlayUrl(@Path("gameAlias") String gameAlias, @QueryMap Map<String, String> map);
}

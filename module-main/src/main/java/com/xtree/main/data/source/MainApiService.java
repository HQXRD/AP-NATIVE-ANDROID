package com.xtree.main.data.source;

import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.base.vo.WsToken;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainApiService {
    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 FB杏彩体育请求服务地址
     * @return
     */
    @POST("/api/sports/fbxc/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBXCGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();

    /**
     * 获取 PM杏彩体育2请求服务地址
     * @return
     */
    @POST("/api/sports/obgzy/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMXCGameTokenApi();

    //==========websocket=============
    @GET("/api/ws/token")
    Flowable<BaseResponse<WsToken>> getWsToken();
    //==========websocket=============
}

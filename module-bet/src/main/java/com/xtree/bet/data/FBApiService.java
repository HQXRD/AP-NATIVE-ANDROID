package com.xtree.bet.data;

import com.xtree.bet.bean.BtConfirmInfo;
import com.xtree.bet.bean.BtConfirmOptionInfo;
import com.xtree.bet.bean.MatchListRsp;
import com.xtree.bet.bean.StatisticalInfo;
import com.xtree.bet.bean.request.BtCarReq;
import com.xtree.bet.bean.request.PBListReq;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface FBApiService {
    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> getFBGameTokenApi();

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/v1/match/getList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<MatchListRsp>> getFBList(@Body PBListReq pbListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/v1/match/statistical")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<StatisticalInfo>> statistical(@Body Map<String, String> map);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/v1/order/batchBetMatchMarketOfJumpLine")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtConfirmInfo>> batchBetMatchMarketOfJumpLine(@Body BtCarReq btCarReq);
}

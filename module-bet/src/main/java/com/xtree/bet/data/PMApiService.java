package com.xtree.bet.data;

import com.xtree.bet.bean.request.BtCarReq;
import com.xtree.bet.bean.request.BtMultipleListReq;
import com.xtree.bet.bean.request.BtRecordReq;
import com.xtree.bet.bean.request.PBListReq;
import com.xtree.bet.bean.request.SingleBtListReq;
import com.xtree.bet.bean.response.BtConfirmInfo;
import com.xtree.bet.bean.response.BtRecordRsp;
import com.xtree.bet.bean.response.BtResultInfo;
import com.xtree.bet.bean.response.LeagueInfo;
import com.xtree.bet.bean.response.MatchInfo;
import com.xtree.bet.bean.response.MatchListRsp;
import com.xtree.bet.bean.response.StatisticalInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface PMApiService {
    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/v1/match/getList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<MatchListRsp>> getFBList(@Body PBListReq pbListReq);
}

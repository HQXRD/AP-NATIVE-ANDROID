package com.xtree.bet.data;

import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.response.pm.MenuInfo;

import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.PMBaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface PMApiService {
    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/yewu11/v1/m/noLiveMatchesPagePB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<MatchListRsp>> noLiveMatchesPagePB(@Body PMListReq pmListReq);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/yewu11/v1/m/liveMatchesPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<MatchInfo>>> liveMatchesPB(@Body PMListReq pmListReq);

    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @GET("/yewu11/pub/v1/m/menu/initPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<MenuInfo>>> initPB();
}

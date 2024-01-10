package com.xtree.bet.data;

import com.xtree.bet.bean.request.pm.BtCarCgReq;
import com.xtree.bet.bean.request.pm.BtCarReq;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.pm.BtConfirmInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.CgOddLimitInfo;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.response.pm.MenuInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.ui.CategoryPm;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.PMBaseResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

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

    /**
     * 获取最新投注数据
     * @return
     */
    @POST("/yewu13/v1/betOrder/client/queryLatestMarketInfo")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<BtConfirmInfo>>> batchBetMatchMarketOfJumpLine(@Body BtCarReq btCarReq);

    /**
     * 查询最大最小投注金额
     * @return
     */
    @POST("/yewu13/v1/betOrder/client/queryMarketMaxMinBetMoney")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<CgOddLimitInfo>>> queryMarketMaxMinBetMoney(@Body BtCarCgReq btCarCgReq);

    /**
     * 详情页获取赛事详情信息
     * @return
     */
    @GET("/yewu11/v1/w/matchDetail/getMatchDetailPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<MatchInfo>> getMatchDetail(@QueryMap Map<String, String> map);

    /**
     * 获取详情玩法集
     * @return
     */
    @GET("/yewu11/v1/w/category/getCategoryList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<CategoryPm>>> getCategoryList(@QueryMap Map<String, String> map);

    /**
     * 获取赛事玩法
     * @return
     */
    @GET("/yewu11/v1/m/matchDetail/getMatchOddsInfoPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<PlayTypeInfo>>> getMatchOddsInfoPB(@QueryMap Map<String, String> map);

    /**
     * 查询最大最小投注金额
     * @return
     */
    @POST("/yewu13/v1/betOrder/client/bet")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<BtResultInfo>> bet(@Body BtReq btReq);

    /**
     * 投注记录接口，按照投注时间查询
     * @return
     */
    @POST("/yewu13/v1/betOrder/client/getOrderListV4PB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<BtRecordRsp>> betRecord(@Body BtRecordReq btRecordReq);

    /**
     * 获取联赛列表
     * @return
     */
    @GET("/yewu11/v1/m/getFilterMatchListPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<LeagueAreaInfo>>> getOnSaleLeagues(@QueryMap Map<String, String> map);
}

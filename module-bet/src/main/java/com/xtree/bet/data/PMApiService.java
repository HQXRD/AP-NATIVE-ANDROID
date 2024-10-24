package com.xtree.bet.data;

import com.xtree.bet.bean.request.pm.BtCarCgReq;
import com.xtree.bet.bean.request.pm.BtCarReq;
import com.xtree.bet.bean.request.pm.BtCashOutBetReq;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.request.pm.BtReq;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.pm.BalanceInfo;
import com.xtree.bet.bean.response.pm.BtCashOutPriceInfo;
import com.xtree.bet.bean.response.pm.BtCashOutStatusInfo;
import com.xtree.bet.bean.response.pm.BtConfirmInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.CgOddLimitInfo;
import com.xtree.bet.bean.response.pm.FrontListInfo;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.response.pm.MenuInfo;
import com.xtree.bet.bean.response.pm.PMResultBean;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.VideoAnimationInfo;
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

public interface PMApiService {
    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/yewu11/v1/m/matchesPagePB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<MatchListRsp>> matchesPagePB(@Body PMListReq pmListReq);
    /**
     * 获取 PM赛事列表 分页获取非滚球赛事信息
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
    Flowable<PMBaseResponse<List<MenuInfo>>> initPB(@QueryMap Map<String, String> map);

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
     * 详情页获取赛果详情信息
     * @return
     */
    @GET("/yewu11/v1/m/matchDetail/getMatchDetailPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<MatchInfo>> getMatchDetailResult(@QueryMap Map<String, String> map);


    /**
     * 获取赛果详情玩法
     */
    @GET("/yewu11/v1/m/matchDetail/getMatchResultPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<PlayTypeInfo>>> getMatchResultPB(@QueryMap Map<String, String> map);

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
     * 查询最大最小投注金额  投注
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

    /**
     * 获取联赛列表
     * @return
     */
    @POST("/yewu11/v1/w/videoAnimationUrlPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<VideoAnimationInfo>> videoAnimationUrlPB(@Body Map<String, String> map);

    /**
     * 获取 PM赛事列表
     * @return
     */
    @POST("/yewu11/v1/m/getMatchBaseInfoByMidsPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<MatchInfo>>> getMatchBaseInfoByMidsPB(@Body PMListReq pmListReq);

    @GET("/yewu12/api/user/amount")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<BalanceInfo>> getUserBanlace(@QueryMap Map<String, String> map);

    /**
     * 提前结算实时查询最高返还批量/批量获取订单提前结算报价
     * @param map
     * @return
     */
    @GET("/yewu13/order/betRecord/getCashoutMaxAmountList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<BtCashOutPriceInfo>>> getCashoutMaxAmountList(@QueryMap Map<String, String> map);

    /**
     * 注单提前结算
     * @param btCashOutBetReq
     * @return
     */
    @POST("/yewu13/v1/betOrder/orderPreSettle")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<BtCashOutPriceInfo>> orderPreSettle(@Body BtCashOutBetReq btCashOutBetReq);

    /**
     * 查询注单提前结算状态
     * @return
     */
    @GET("/yewu13/v1/betOrder/queryOrderPreSettleConfirm")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<BtCashOutStatusInfo>>> queryOrderPreSettleConfirm();

    /**
     * 公告列表集合
     */
    @POST("/yewu11/v2/notice/frontListPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<FrontListInfo>> frontListPB();

    /**
     * 赛果菜单统计
     */
    @GET("/yewu11/v2/m/menu/resultMenuPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<PMResultBean>>> resultMenuPB(@QueryMap Map<String, String> map);

    /**
     * 获取赛果信息赛事列表
     */
    @POST("/yewu11/v1/m/matcheResultPB")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<PMBaseResponse<List<MatchInfo>>> matcheResultPB(@Body Map<String, String> map);
}

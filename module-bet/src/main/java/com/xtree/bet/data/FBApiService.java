package com.xtree.bet.data;

import com.xtree.bet.bean.request.fb.BtCashOutBetReq;
import com.xtree.bet.bean.request.fb.BtCashOutPriceReq;
import com.xtree.bet.bean.request.fb.BtCashOutStatusReq;
import com.xtree.bet.bean.request.fb.BtMultipleListReq;
import com.xtree.bet.bean.request.fb.BtRecordReq;
import com.xtree.bet.bean.request.fb.SingleBtListReq;
import com.xtree.bet.bean.response.fb.BalanceInfo;
import com.xtree.bet.bean.response.fb.BtCashOutBetInfo;
import com.xtree.bet.bean.response.fb.BtCashOutPriceInfo;
import com.xtree.bet.bean.response.fb.BtCashOutStatusInfo;
import com.xtree.bet.bean.response.fb.BtConfirmInfo;
import com.xtree.bet.bean.response.fb.BtRecordRsp;
import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.response.fb.LeagueInfo;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.bean.response.fb.StatisticalInfo;
import com.xtree.bet.bean.request.fb.BtCarReq;
import com.xtree.bet.bean.request.fb.FBListReq;

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

public interface FBApiService {

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/v1/match/getList")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<MatchListRsp>> getFBList(@Body FBListReq FBListReq);

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
    /**
     * 按运动、分类类型统计可投注的赛事个数
     * @return
     */
    @POST("/v1/match/getMatchDetail")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<MatchInfo>> getMatchDetail(@Body Map<String, String> map);
    /**
     * 单关投注
     * @return
     */
    @POST("/v1/order/bet/singlePass")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<BtResultInfo>>> singlePass(@Body SingleBtListReq req);
    /**
     * 串关投注
     * @return
     */
    @POST("/v1/order/betMultiple")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<BtResultInfo>>> betMultiple(@Body BtMultipleListReq req);
    /**
     * 投注记录接口，并按币种统计
     * @return
     */
    @POST("/v1/order/new/bet/list")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtRecordRsp>> betRecord(@Body BtRecordReq btRecordReq);
    /**
     * 获取联赛列表
     * @return
     */
    @POST("/v1/match/getOnSaleLeagues")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<LeagueInfo>>> getOnSaleLeagues(@Body Map<String, String> map);

    /**
     * 用户查询余额
     * @param map
     * @return
     */
    @POST("/v1/user/base")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BalanceInfo>> getUserBanlace(@Body Map<String, String> map);

    /**
     * 批量获取订单提前结算报价
     * @param btCashOutPriceReq
     * @return
     */
    @POST("/v1/order/cashOut/price")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtCashOutPriceInfo>> cashOutPrice(@Body BtCashOutPriceReq btCashOutPriceReq);

    /**
     * 提前结算下注
     * @param btCashOutPriceBetReq
     * @return
     */
    @POST("/v1/order/cashOut/bet")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<BtCashOutBetInfo>> cashOutPriceBet(@Body BtCashOutBetReq btCashOutPriceBetReq);

    /**
     * 提前结算下注
     * @param btCashOutStatusReq
     * @return
     */
    @POST("/v1/order/getCashOutsByIds")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Flowable<BaseResponse<List<BtCashOutStatusInfo>>> getCashOutsByIds(@Body BtCashOutStatusReq btCashOutStatusReq);

}

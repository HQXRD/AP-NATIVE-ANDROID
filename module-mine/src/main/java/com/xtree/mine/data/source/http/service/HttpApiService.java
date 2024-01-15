package com.xtree.mine.data.source.http.service;

import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.mine.vo.AccountChangeVo;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.BankCardVo;
import com.xtree.mine.vo.BtDetailVo;
import com.xtree.mine.vo.BtPlatformVo;
import com.xtree.mine.vo.BtReportVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.ProfileVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.RebateReportVo;
import com.xtree.mine.vo.RechargeReportVo;
import com.xtree.mine.vo.SettingsVo;
import com.xtree.mine.vo.ThirdGameTypeVo;
import com.xtree.mine.vo.ThirdTransferReportVo;
import com.xtree.mine.vo.UsdtVo;
import com.xtree.mine.vo.UserBankConfirmVo;
import com.xtree.mine.vo.UserBankProvinceVo;
import com.xtree.mine.vo.UserBindBaseVo;
import com.xtree.mine.vo.UserUsdtConfirmVo;
import com.xtree.mine.vo.UserUsdtTypeVo;
import com.xtree.mine.vo.VerificationCodeVo;
import com.xtree.mine.vo.VerifyVo;

import java.util.HashMap;
import java.util.List;
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
import retrofit2.http.Query;
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
    Flowable<BaseResponse<LoginResultVo>> register(@Body Map<String, String> map);

    @GET("/api/settings/?")
    Flowable<BaseResponse<SettingsVo>> getSettings(@QueryMap Map<String, String> filters);

    /**
     * 获取 个人信息
     */
    @GET("/api/account/profile")
    Flowable<BaseResponse<ProfileVo>> getProfile();

    /**
     * 发送 短信/邮箱 验证码 (首次绑)
     */
    @GET("/api/verify/singlesend")
    Flowable<BaseResponse<VerificationCodeVo>> singleSend(
            @Query("flag") String flag,
            @Query("sendtype") String sendtype,
            @Query("num") String num);

    /**
     * 验证 短信/邮箱 验证码 (首次绑)
     */
    @POST("/api/verify/singleverify")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<VerifyVo>> singleVerify(@Body Map<String, String> map);

    /**
     * 自我更新手机邮箱 （验证码和确定都走这个）
     */
    @POST("/api/verify/updateverify")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<VerifyVo>> updateVerify(@Body Map<String, String> map);

    /**
     * 手机和邮箱的互相绑定 （验证码和确定都走这个）
     */
    @POST("/api/verify/bindverify")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<VerifyVo>> bindVerify(@Body Map<String, String> map);

    /**
     * 修改密码
     */
    @POST("/api/account/verifychangepassword")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Map<String, String>>> changePwd(@Body Map<String, String> map);

    /**
     * 获取 平台中心余额
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
     * 一键回收
     */
    @GET("/api/game/transferOutAllThirdGame")
    Flowable<BaseResponse<Object>> do1kAutoRecycle();

    /**
     * 开启/关闭 自动免转
     *
     * @param map 参数 (status,userid,nonce)
     */
    @POST("/api/game/setthrad")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> doAutoTransfer(@Body Map<String, String> map);

    /**
     * 转账
     *
     * @param map 参数 (from,to,money,nonce)
     */
    @POST("/api/game/transfer")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> doTransfer(@Body Map<String, String> map);

    /**
     * 获取用户已绑定的银行卡列表
     * /user/userbankinfo?check=***&mark=bindcard&client=m
     */
    @GET("/user/userbankinfo?")
    Flowable<UserBindBaseVo<BankCardVo>> getBankCardList(@QueryMap Map<String, String> map);

    /**
     * 获取银行列表/省列表
     * /user/userbankinfo?check=***&mark=bindcard&client=m&controller=security&action=adduserbank
     */
    @GET("/user/userbankinfo?")
    Flowable<UserBankProvinceVo> getBankProvinceList(@QueryMap Map<String, String> map);

    /**
     * 获取 市列表
     */
    @POST("/user/userbankinfo/?controller=security&action=adduserbank&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<HashMap<String, List<UserBankProvinceVo.AreaVo>>> getCityList(@Body Map<String, String> map);

    /**
     * 绑定银行卡
     *
     * @param queryMap URL拼装用的 controller=security&action=adduserbank&client=m&mark=bindcard&check=***
     * @param map      POST Body 体用的
     */
    @POST("/user/userbankinfo/?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<UserBankConfirmVo> doBindBankCard(@QueryMap Map<String, String> queryMap, @Body Map<String, String> map);

    /**
     * 锁定银行卡
     * /security/deluserbank?flag=lock&id=***&client=m
     */
    @POST("/security/deluserbank?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<UserBankConfirmVo> delBankCard(@QueryMap Map<String, String> queryMap, @Body Map<String, String> map);

    /**
     * 查询已绑定的列表
     * client=m&check=***&mark=bindusdt
     */
    @GET("/user/user{key}info?")
    Flowable<UserBindBaseVo<UsdtVo>> getUsdtList(@Path("key") String key, @QueryMap Map<String, String> map);

    /**
     * 查询 USDT 类型
     * client=m&controller=security&action=adduserusdt
     */
    @GET("/user/user{key}info?")
    Flowable<UserUsdtTypeVo> getUsdtType(@Path("key") String key, @QueryMap Map<String, String> map);

    /**
     * 绑定
     * client=m&controller=security&action=adduserusdt
     */
    @POST("/user/user{key}info?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<UserUsdtConfirmVo> doBindUsdt(@Path("key") String key, @QueryMap Map<String, String> queryMap, @Body Map<String, String> map);

    /**
     * 重新绑定
     */
    @POST("/security/{key}rebinding?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<UserUsdtConfirmVo> doRebindUsdt(@Path("key") String key, @QueryMap Map<String, String> queryMap, @Body Map<String, String> map);

    /**
     * 账变记录
     * starttime=2024-01-09 00:00:00&endtime=2024-01-10 23:59:59&ordertype=0&status=0&p=1&pn=20&client=m
     */
    @GET("/report/selfbankreport?")
    Flowable<AccountChangeVo> getAccountChangeReport(@QueryMap Map<String, String> map);

    /**
     * 盈亏报表-查类型
     */
    @GET("/api/game/thirdgametype?")
    Flowable<BaseResponse<List<ThirdGameTypeVo>>> getThirdGameType();

    /**
     * 盈亏报表-查列表
     */
    @GET("/gameinfo/eprofitlossNew?")
    Flowable<ProfitLossReportVo> getProfitLoss(@QueryMap Map<String, String> map);

    /**
     * 返水报表-查列表
     * ?controller=compact&action=userantireport&client=m
     * &starttime=2024-01-10 00:00:00&endtime=2024-01-11 23:59:59&type=0&pstatus=0&p=1&pn=15&orderby=date&sort=desc
     */
    @GET("/?")
    Flowable<RebateReportVo> getRebateReport(@QueryMap Map<String, String> map);

    /**
     * 第三方转账
     * ?controller=report&action=fundreport&isgetdata=1&client=m
     * &starttime=2024-01-10 00:00:00&endtime=2024-01-11 23:59:59&out_money=all&in_money=all&status=all&p=1&page_size=10
     */
    @GET("/?")
    Flowable<ThirdTransferReportVo> getThirdTransferReport(@QueryMap Map<String, String> map);

    /**
     * 充值记录
     * ?starttime=2024-01-10 00:00:00&endtime=2024-01-11 23:59:59&p=1&pn=10&client=m
     */
    @GET("/report/emailreport?")
    Flowable<RechargeReportVo> getRechargeReport(@QueryMap Map<String, String> map);

    /**
     * 提款记录
     * ?starttime=2024-01-10 00:00:00&endtime=2024-01-11 23:59:59&p=1&pn=10&client=m
     */
    @GET("/report/withdrawalreport?")
    Flowable<RechargeReportVo> getWithdrawReport(@QueryMap Map<String, String> map);

    /**
     * 未到账反馈记录
     * ?starttime=2023-10-10 00:00&endtime=2024-01-11 23:59&p=1&page_size=10&client=m
     */
    @GET("/api/deposit/customerinfos?")
    Flowable<BaseResponse<RechargeReportVo>> getFeedbackReport(@QueryMap Map<String, String> map);

    /**
     * 投注记录-查类型 (同 盈亏报表-查类型)
     */
    @GET("/api/game/thirdgametype?")
    Flowable<BaseResponse<List<BtPlatformVo>>> getBtPlatformType();

    /**
     * 投注记录-列表
     * ?isgetdata=1&userid=2888826&startDate=2023-11-12 00:00:00&endDate=2024-01-12 23:59:59
     * &platform=&ischild=0&p=1&bet_result=0&pn=20&client=m
     */
    @GET("/gameinfo/allProjectsNew?")
    Flowable<BtReportVo> getBtReport(@QueryMap Map<String, String> map);

    /**
     * 投注记录-详情
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @POST("/api/game/projectDetail?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<BtDetailVo>> getBtOrderDetail(@Body Map<String, String> map);

    /**
     * 获取 FB体育请求服务地址
     */
    @POST("/api/sports/fb/getToken")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     */
    @POST("/api/sports/obg/getToken")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PMService>> getPMGameTokenApi();
}

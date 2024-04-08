package com.xtree.mine.data.source.http.service;

import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.AppUpdateDialog;
import com.xtree.mine.vo.AccountChangeVo;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.BankCardCashMoYuVo;
import com.xtree.mine.vo.BankCardCashVo;
import com.xtree.mine.vo.BankCardVo;
import com.xtree.mine.vo.BtDetailVo;
import com.xtree.mine.vo.BtPlatformVo;
import com.xtree.mine.vo.BtReportVo;
import com.xtree.mine.vo.CheckQuestionVo;
import com.xtree.mine.vo.ChooseInfoMoYuVo;
import com.xtree.mine.vo.ChooseInfoVo;
import com.xtree.mine.vo.CookieVo;
import com.xtree.mine.vo.ForgetPasswordCheckInfoVo;
import com.xtree.mine.vo.ForgetPasswordTimeoutVo;
import com.xtree.mine.vo.ForgetPasswordVerifyVo;
import com.xtree.mine.vo.FundPassWordVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameChangeVo;
import com.xtree.mine.vo.GooglePswVO;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.LotteryDetailVo;
import com.xtree.mine.vo.LotteryReportVo;
import com.xtree.mine.vo.MarketingVo;
import com.xtree.mine.vo.MemberManagerVo;
import com.xtree.mine.vo.MsgInfoVo;
import com.xtree.mine.vo.MsgListVo;
import com.xtree.mine.vo.MsgPersonInfoVo;
import com.xtree.mine.vo.MsgPersonListVo;
import com.xtree.mine.vo.PlatWithdrawConfirmMoYuVo;
import com.xtree.mine.vo.PlatWithdrawConfirmVo;
import com.xtree.mine.vo.PlatWithdrawMoYuVo;
import com.xtree.mine.vo.PlatWithdrawVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.QuestionVo;
import com.xtree.mine.vo.RebateReportVo;
import com.xtree.mine.vo.RechargeReportVo;
import com.xtree.mine.vo.SendMoneyVo;
import com.xtree.mine.vo.SettingsVo;
import com.xtree.mine.vo.ThirdGameTypeVo;
import com.xtree.mine.vo.ThirdTransferReportVo;
import com.xtree.mine.vo.USDTCashMoYuVo;
import com.xtree.mine.vo.USDTCashVo;
import com.xtree.mine.vo.USDTConfirmMoYuVo;
import com.xtree.mine.vo.USDTConfirmVo;
import com.xtree.mine.vo.USDTSecurityMoYuVo;
import com.xtree.mine.vo.USDTSecurityVo;
import com.xtree.mine.vo.UsdtVo;
import com.xtree.mine.vo.UserBankConfirmVo;
import com.xtree.mine.vo.UserBankProvinceVo;
import com.xtree.mine.vo.UserBindBaseVo;
import com.xtree.mine.vo.UserUsdtConfirmVo;
import com.xtree.mine.vo.VerificationCodeVo;
import com.xtree.mine.vo.VerifyVo;
import com.xtree.mine.vo.VipUpgradeInfoVo;
import com.xtree.mine.vo.VirtualCashMoYuVo;
import com.xtree.mine.vo.VirtualCashVo;
import com.xtree.mine.vo.VirtualConfirmMoYuVo;
import com.xtree.mine.vo.VirtualConfirmVo;
import com.xtree.mine.vo.VirtualSecurityMoYuVo;
import com.xtree.mine.vo.VirtualSecurityVo;
import com.xtree.mine.vo.request.AdduserRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.BaseResponse2;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
     * 获取 cookie,session
     */
    @GET("/api/auth/sessid?client_id=10000005")
    Flowable<BaseResponse<CookieVo>> getCookie();

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
     * 发验证码 (异地登录/换设备登录)
     */
    @GET("/api/auth/sendCode")
    Flowable<BaseResponse<VerificationCodeVo>> sendCodeByLogin(@QueryMap Map<String, String> map);

    /**
     * 修改登录密码
     */
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    @PUT("/api/account/password")
    Flowable<BaseResponse<Map<String, String>>> changeLoginPwd(@Body Map<String, String> map);

    /**
     * 修改资金密码
     */
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    @PUT("/api/account/security-password")
    Flowable<BaseResponse<Map<String, String>>> changeFundsPwd(@Body Map<String, String> map);

    /**
     * 获取谷歌密钥文本格式
     */
    @GET("/api/two-fa/make-key")
    Flowable<BaseResponse<GooglePswVO>> getGoogle();

    /**
     * 绑定谷歌动态口令
     */
    @POST("/api/two-fa/save")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> bindGoogle(@Body Map<String, String> map);

    /**
     * 谷歌验证 code,nonce
     */
    @POST("/api/two-fa/verify")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> authGoogleCode(@Body Map<String, String> map);

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

    ///**
    // * 查询 USDT 类型
    // * client=m&controller=security&action=adduserusdt
    // */
    //@GET("/user/user{key}info?")
    //Flowable<UserUsdtTypeVo> getUsdtType(@Path("key") String key, @QueryMap Map<String, String> map);

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
     * 游戏账变记录
     * report/lotteryrecord?&starttime=2024-03-15%2000:00:00&endtime=2024-03-16%2023:59:59&ordertype=0&p=1&page_size=20
     */
    @GET("/report/lotteryrecord?")
    Flowable<GameChangeVo> getGameChangeReport(@QueryMap Map<String, String> map);

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
     * 投注记录-列表(彩票)
     * ?controller=gameinfo&action=newgamelist&starttime=2023-09-13 00:00:00&endtime=2024-02-13 23:59:59
     * &lotteryid=0&methodid=0&p=1&pn=20&ischild=0&client=m
     */
    @GET("/gameinfo")
    Flowable<LotteryReportVo> getCpReport(@QueryMap Map<String, String> map);

    /**
     * 投注记录-详情
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @POST("/api/game/projectDetail?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<BtDetailVo>> getBtOrderDetail(@Body Map<String, String> map);

    /**
     * 投注记录-详情(彩票)
     * platform=FBXC,project_id=10950255273****7510,nonce=***
     */
    @GET("/gameinfo/newgamedetail/{id}?client=m")
    Flowable<LotteryDetailVo> getBtCpOrderDetail(@Path("id") String id);

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

    /**
     * 获取 用户手机与信箱
     */
    @POST("/api/account/forgetpassword")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<ForgetPasswordCheckInfoVo>> getUserInfoApi(@Body Map<String, String> map);

    /**
     * 获取 otp的资讯
     */
    @POST("/api/account/forgetpassword")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<ForgetPasswordTimeoutVo>> getForgetPasswordOTP(@Body Map<String, String> map);

    /**
     * 获取 用户资讯正确的token
     */
    @POST("/api/account/forgetpassword")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<ForgetPasswordVerifyVo>> getUserTokenApi(@Body Map<String, String> map);

    /**
     * 获取 修改密码
     */
    @POST("/api/account/forgetpassword")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> getChangePasswordResult(@Body Map<String, String> map);

    /**
     * 获取 消息列表
     */
    @GET("/api/notice/list?")
    Flowable<BaseResponse<MsgListVo>> getMessageList(@QueryMap Map<String, String> map);

    /**
     * 获取 消息详情
     */
    @GET("/api/notice/{key}")
    Flowable<BaseResponse<MsgInfoVo>> getMessage(@Path("key") String key);

    /**
     * 获取 个人消息
     */
    @GET("/api/message/list?")
    Flowable<BaseResponse<MsgPersonListVo>> getMessagePersonList(@QueryMap Map<String, String> map);

    /**
     * 获取 消息详情
     */
    @GET("/api/message/{key}")
    Flowable<BaseResponse<MsgPersonInfoVo>> getMessagePerson(@Path("key") String key);

    /**
     * 获取 消息详情
     */
    @GET("/api/account/vipinfo")
    Flowable<BaseResponse<VipUpgradeInfoVo>> getVipUpgradeInfo();

    /**
     * 获取提款方式
     */
    @GET("/security/platwithdraw?1=1&client=m")
    Flowable<ChooseInfoVo> getChooseWithdrawInfo();

    /**
     * 银行卡 获取提款方式
     */
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&check=&ismobile=true&usdt_type=1&is_tutorial=1&client=m")
    Flowable<BankCardCashVo> getChooseWithdrawBankDetailsInfo();

    /**
     * 银行卡提款提交
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PlatWithdrawVo> postPlatWithdrawBank(@Body Map<String, String> map);

    /**
     * 银行卡确认提交
     *
     * @POST("/security/platwithdraw?1=1&client=m") https://ap3sport.oxldkm.com/security/index.php
     * @POST("/security/index.php?1=1&client=m")
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PlatWithdrawConfirmVo> postConfirmWithdrawBank(@Body Map<String, String> map);

    /**
     * USDT获取提款方式
     */
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&check=&ismobile=true&is_tutorial=1&client=m")
    Flowable<USDTCashVo> getChooseWithdrawUSDT(@QueryMap Map<String, String> map);

    /**
     * USDT提款提交
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<USDTSecurityVo> postPlatWithdrawUSDT(@Body Map<String, String> map);

    /**
     * USDT确认提交
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<USDTConfirmVo> postConfirmWithdrawUSDT(@Body Map<String, String> map);

    /**
     * 虚拟币获取提款方式
     */
    //https://ap3sport.oxldkm.com/security/platwithdraw/?controller=security&action=platwithdraw&check=&ismobile=true&usdt_type=4&is_tutorial=1&client=m
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&check=&ismobile=true&is_tutorial=1&client=m")
    Flowable<VirtualCashVo> getChooseWithdrawVirtual(@QueryMap Map<String, String> map);

    /**
     * 虚拟币提款提交
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<VirtualSecurityVo> postPlatWithdrawVirtual(@Body Map<String, String> map);

    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<VirtualSecurityVo> postPlatWithdrawVirtual(@QueryMap Map<String, String> queryMap, @Body Map<String, String> map);

    /**
     * 虚拟币确认提交
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<VirtualConfirmVo> postConfirmWithdrawVirtual(@Body Map<String, String> map);

    @GET("/help/answer?client=m")
    Flowable<QuestionVo> getQuestionWeb();

    /**
     * 获取流水
     */
    @GET("/api/activity/awardrecord?&client=m")
    Flowable<BaseResponse<AwardsRecordVo>> getAwardRecord();

    @POST("/?controller=user&action=messages&tag=deleteselect&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse2> deletePartPersonInfo(@Body Map<String, Object> map);

    @GET("/user/messages?tag=deleteall&client=m")
    Flowable<BaseResponse2> deleteAllPersonInfo();

    @GET("/user/list/?frame=show&client=m")
    Flowable<MemberManagerVo> getMemberManager(@QueryMap Map<String, String> map);

    @POST("/?controller=security&action=checkpass&client=m")
    Flowable<Object> checkMoneyPassword(@Body Map<String, String> map);

    @POST("/user/?controller=user&action=saveup&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<SendMoneyVo> sendMoney(@Body Map<String, String> map);

    /*输入资金密码（魔域）*/
    @POST("/?controller=security&action=checkpass&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<FundPassWordVo> getCheckPass(@Body HashMap<String, String> map);

    /*获取提款方式(魔域)*/
    @GET("/security/platwithdraw?client=m")
    Flowable<ChooseInfoMoYuVo> getChooseWithdrawInfo(@Query("check") String flag);

    /* 银行卡 获取提款方式（魔域）*/
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&ismobile=true&usdt_type=1&is_tutorial=1&client=m")
    Flowable<BankCardCashMoYuVo> getChooseWithdrawBankDetailsInfo(@Query("check") String flag);

    /*银行卡提款信息确认 （魔域）*/
    @POST("/security/platwithdraw?client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PlatWithdrawMoYuVo> postMoYuPlatWithdrawBank(@Body Map<String, String> map);

    /*
     * 银行卡确认提交 完成【魔域】
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<PlatWithdrawConfirmMoYuVo> postMoYuConfirmWithdrawBank(@Body Map<String, String> map);

    /*
     * USDT获取提款方式【魔域】
     */
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&ismobile=true&is_tutorial=1&client=m")
    Flowable<USDTCashMoYuVo> getMoYuChooseWithdrawUSDT(@Query("check") String key, @Query("usdt_type") String usdtType);

    /*
     * USDT提款提交【魔域】
     */
    @POST("/security/platwithdraw?client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<USDTSecurityMoYuVo> postMoYuPlatWithdrawUSDT(@Body Map<String, String> map);

    /*
     * USDT确认提交【魔域】
     */
    @POST("/security/platwithdraw?client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<USDTConfirmMoYuVo> postMoYuConfirmWithdrawUSDT(@Body Map<String, String> map);

    /**
     * 虚拟币获取提款方式 【魔域】
     */
    @GET("/security/platwithdraw/?controller=security&action=platwithdraw&ismobile=true&is_tutorial=1&client=m")
    Flowable<VirtualCashMoYuVo> getMoYuChooseWithdrawVirtual(@Query("check") String key, @Query("usdt_type") String flag);

    /**
     * 虚拟币提款提交【魔域】
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<VirtualSecurityMoYuVo> postMoYuPlatWithdrawVirtual(@Body Map<String, String> map);

    /**
     * 虚拟币确认提交 【魔域】
     */
    @POST("/security/platwithdraw?1=1&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<VirtualConfirmMoYuVo> postMoYuConfirmWithdrawVirtual(@Body Map<String, String> map);

    /**
     * 返水契约 GET
     *
     * @param url 接口名称
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url);

    /**
     * 返水契约 GET
     *
     * @param url 接口名称
     * @param map 拼接参数
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> map);

    /**
     * 返水契约 POST
     *
     * @param url 接口名称
     * @param map body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> map);

    /**
     * 返水契约 POST
     *
     * @param url  接口名称
     * @param qmap 拼接参数
     * @param map  body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> qmap, @Body Map<String, Object> map);

    /*魔域 设置密保*/
    @PUT("/api/account/security-questions")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse2> putSecurityQuestions(@Body Map<String, Object> map);

    /*魔域 输入密保问题校验*/
    @POST("/?controller=user&action=checkquestion&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<CheckQuestionVo> postCheckQuestion(@Body Map<String, String> map);

    /*魔域 密码找回*/
    @POST("/?controller=user&action=retrievesepas&client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse2> postRetrievePSW(@Body Map<String, String> map);

    /**
     * 推广链接-获取数据
     */
    @GET("/user/marketing?client=m")
    Flowable<MarketingVo> marketing();

    /**
     * 推广链接-保存更新
     */
    @POST("/user/marketing?client=m")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<MarketingVo> postMarketing(@Body Map<String, String> map);

    /**
     * 注册开户-创建用户
     */
    @POST("/user/adduser?client=m")
    Flowable<BaseResponse2> adduser(@Body AdduserRequest requestBody);

    /**
     * 获取更新
     */
    @GET("/api/app/version?platform=android")
    Flowable<BaseResponse<AppUpdateDialog.AppUpdateVo>> getUpdate();

    @POST("/api/user/verifylastbind?")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> verifyAcc(@QueryMap Map<String, Object> qMap, @Body Map<String, Object> map);
}

package com.xtree.mine.data.source.http.service;

import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.LoginResultVo;
import com.xtree.mine.vo.ProfileVo;
import com.xtree.mine.vo.SettingsVo;
import com.xtree.mine.vo.VerificationCodeVo;
import com.xtree.mine.vo.VerifyVo;

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

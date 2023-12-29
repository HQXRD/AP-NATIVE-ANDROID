package com.xtree.recharge.data.source;

import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Flowable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 获取 一键进入 hiwallet钱包<br>
     * "login_url": "https://hiwalletH5.com/signup?qy=1&platformCode=AS&platformUserId=28***26&userEmail="
     *
     * @return
     */
    @POST("/api/deposit/payments/login/hiwallet")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Map<String, String>>> get1kEntry(@Body Map<String, String> map);

    /**
     * 获取 充值列表
     *
     * @return
     */
    @GET("/api/deposit/payments?getinfo=true")
    Flowable<BaseResponse<PaymentVo>> getPayments();

    /**
     * 获取 充值类型详情 (跳转链接用的)
     *
     * @param bid
     * @return
     */
    @GET("/api/deposit/payments?")
    Flowable<BaseResponse<RechargeVo>> getPayment(@Query("bid") String bid);

    @POST("/api/deposit/rechargepay/{bid}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> rechargePay(@Path("bid") String bid, @Body Map<String, String> map);

}

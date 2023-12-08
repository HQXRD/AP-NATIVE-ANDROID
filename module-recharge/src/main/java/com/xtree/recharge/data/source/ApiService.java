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
    //Flowable<BaseResponse<RechargeVo>> getPayment(@QueryMap Map<String, String> map);

    @POST("/api/deposit/rechargepay/{bid}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<RechargePayVo>> rechargePay(@Path("bid") String bid, @Body Map<String, String> map);

}

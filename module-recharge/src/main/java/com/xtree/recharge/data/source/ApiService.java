package com.xtree.recharge.data.source;

import com.xtree.recharge.vo.BannersVo;
import com.xtree.recharge.vo.FeedbackCheckVo;
import com.xtree.recharge.vo.FeedbackImageUploadVo;
import com.xtree.recharge.vo.FeedbackVo;
import com.xtree.recharge.vo.PayOrderDataVo;
import com.xtree.recharge.vo.PaymentDataVo;
import com.xtree.recharge.vo.PaymentVo;
import com.xtree.recharge.vo.RechargeOrderDetailVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    /**
     * GET
     *
     * @param url 接口名称
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url);

    /**
     * GET
     *
     * @param url 接口名称
     * @param map 拼接参数
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> map);

    /**
     * POST
     *
     * @param url 接口名称
     * @param map body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> map);

    /**
     * POST
     *
     * @param url  接口名称
     * @param qmap 拼接参数
     * @param map  body
     * @return 返回体
     */
    @POST("{url}")
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> qmap, @Body Map<String, Object> map);


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
     * 获取 充值列表(分大小类)
     */
    @GET("/api/deposit/paymentsclassify?getinfo=true&source=11")
    Flowable<BaseResponse<PaymentDataVo>> getPaymentsTypeList();

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

    /**
     * 获取 实际充值金额 (接口和充值一样,参数不一样)
     */
    @POST("/api/deposit/rechargepay/{bid}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> getRealMoney(@Path("bid") String bid, @Body Map<String, String> map);

    /**
     * 提交充值
     */
    @POST("/api/deposit/rechargepay/{bid}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> rechargePay(@Path("bid") String bid, @Body Map<String, String> map);

    /**
     * 提交充值 (极速充值)
     *
     * @param map
     * @return
     */
    @GET("/api/deposit/createorder")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<PayOrderDataVo>> createOrder(@QueryMap Map<String, String> map);

    /**
     * 获取人工充值暗号
     */
    @GET("/api/deposit/manualcode")
    Flowable<BaseResponse<Map<String, String>>> getManualSignal();

    /**
     * 获取充值订单详情
     */
    @GET("/api/deposit/rechargeinfo")
    Flowable<BaseResponse<RechargeOrderDetailVo>> getOrderDetail(@Query("id") String id);

    /**
     * 获取轮播图(充值页的)
     */
    @GET("/api/bns/12/banners?limit=20")
    Flowable<BaseResponse<List<BannersVo>>> getRechargeBanners();

    /**
     * 获取反馈页面基本数据
     */
    @GET("/api/deposit/customerinfos?")
    Flowable<BaseResponse<FeedbackVo>> getFeedback(@QueryMap Map<String, String> map);

    /**
     * 查看反馈详情
     */
    @GET("/api/deposit/customerinfos?")
    Flowable<BaseResponse<FeedbackCheckVo>> getEditFeedback(@QueryMap Map<String, String> map);

    /**
     * 反馈页面上传图片接口
     */
    @POST("/api/fileuseapi/upload")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FeedbackImageUploadVo>> feedbackFileUpLoad(@Body Map<String, String> map);

    /**
     * 查询获取查询反馈页面配置
     */
    @GET("/api/deposit/customerinfos?")
    Flowable<BaseResponse<FeedbackVo>> feedbackCheckInfo(@Query("starttime") String starttime, @Query("endtime") String endtime);

    /**
     * 查询获取反馈页面详情数据（充值记录->查看）
     *
     * @param id 选择反馈记录id
     */
    @GET("/api/deposit/customerinfos?client=m")
    Flowable<BaseResponse<FeedbackCheckVo>> feedbackCheckDetailsInfo(@Query("id") String id);

    /**
     * 反馈页面上传
     *
     * @param map
     */
    @POST("/api/deposit/customerinfoadd")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<Object>> feedbackCustomAdd(@Body Map<String, String> map);

    /**
     * 反馈页面 查看图片下载
     *
     * @param starttime
     */
    @GET("/api/deposit/customerinfos?")
    Flowable<BaseResponse<Object>> feedbackCheckImage(@Query("starttime") String starttime);

}

package com.xtree.recharge.data.source;


import com.xtree.recharge.data.source.request.ExBankInfoRequest;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.data.source.request.ExOrderCancelRequest;
import com.xtree.recharge.data.source.request.ExReceiptUploadRequest;
import com.xtree.recharge.data.source.request.ExReceiptocrRequest;
import com.xtree.recharge.data.source.request.ExRechargeOrderCheckRequest;
import com.xtree.recharge.data.source.response.ExBankInfoResponse;
import com.xtree.recharge.data.source.response.ExCreateOrderResponse;
import com.xtree.recharge.data.source.response.ExReceiptUploadResponse;
import com.xtree.recharge.data.source.response.ExReceiptocrResponse;
import com.xtree.recharge.data.source.response.ExRechargeOrderCheckResponse;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    //Flowable<BaseResponse<Object>> login(String username, String password);

    ApiService getApiService();

    /**
     * 充值建单申请
     */
    Flowable<BaseResponse<ExCreateOrderResponse>> createorder(ExCreateOrderRequest request);
    /**
     * 充值取消等待
     */
    Flowable<BaseResponse> cancelOrderWait(ExOrderCancelRequest request);
    /**
     * 充值取消审核
     */
    Flowable<BaseResponse> cancelOrderProcess(ExOrderCancelRequest request);

    /**
     * 充值银行卡信息
     */
    Flowable<BaseResponse<ExBankInfoResponse>> rechargeBankInfo(ExBankInfoRequest request);

    /**
     * 识别凭证信息ocr
     */
    Flowable<BaseResponse<ExReceiptocrResponse>> rechargeReceiptOCR(ExReceiptocrRequest request);

    /**
     * 上传付款凭证
     */
    Flowable<BaseResponse<ExReceiptUploadResponse>> rechargeReceiptUpload(ExReceiptUploadRequest request);

    /**
     * 查询订单信息
     */
    Flowable<ExRechargeOrderCheckResponse> rechargeOrderCheck(ExRechargeOrderCheckRequest request);


    /**
     * 新增跳过引导接口
     * 引导极速充值接口使用
     */
    @GET("api/deposit/skipOnepayfixGuide?")
    Flowable<BaseResponse<Object>> skipGuide();
}

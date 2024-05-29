package com.xtree.recharge.data.source.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.recharge.data.source.APIManager;
import com.xtree.recharge.data.source.ApiService;
import com.xtree.recharge.data.source.HttpDataSource;
import com.xtree.recharge.data.source.request.ExBankInfoRequest;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.data.source.request.ExOrderCancelRequest;
import com.xtree.recharge.data.source.request.ExReceiptUploadRequest;
import com.xtree.recharge.data.source.request.ExReceiptocrRequest;
import com.xtree.recharge.data.source.request.ExRechargeOrderCheckRequest;
import com.xtree.recharge.data.source.response.ExBankInfoResponse;
import com.xtree.recharge.data.source.response.ExCreateOrderResponse;
import com.xtree.recharge.data.source.response.ExReceiptocrResponse;
import com.xtree.recharge.data.source.response.ExRechargeOrderCheckResponse;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {

    private static TypeReference<Map<String, Object>> type;
    private ApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                    type = new TypeReference<Map<String, Object>>() {
                    };
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    /*@Override
    public Flowable<BaseResponse<Object>> login(String username, String password) {
        return apiService.login(username, password);
    }*/

    @Override
    public ApiService getApiService() {
        return apiService;
    }

    @Override
    public Flowable<BaseResponse<ExCreateOrderResponse>> createorder(ExCreateOrderRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.DEPOSIT_CREATEORDER_URL,map).map(new Function<ResponseBody, BaseResponse<ExCreateOrderResponse>>() {
            @Override
            public BaseResponse<ExCreateOrderResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<ExCreateOrderResponse>>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse> cancelOrderWait(ExOrderCancelRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.DEPOSIT_CANCELWAIT_URL,map).map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse> cancelOrderProcess(ExOrderCancelRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.DEPOSIT_CANCELPROCESS_URL,map).map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse<ExBankInfoResponse>> rechargeBankInfo(ExBankInfoRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.DEPOSIT_RECHARGEBANKINFO_URL,map).map(new Function<ResponseBody, BaseResponse<ExBankInfoResponse>>() {
            @Override
            public BaseResponse<ExBankInfoResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<ExBankInfoResponse>>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse<ExReceiptocrResponse>> rechargeReceiptOCR(ExReceiptocrRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.post(APIManager.DEPOSIT_RECHARGERECEIPTOCR_URL,map).map(new Function<ResponseBody, BaseResponse<ExReceiptocrResponse>>() {
            @Override
            public BaseResponse<ExReceiptocrResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<ExReceiptocrResponse>>() {
                        });
            }
        });
    }

    @Override
    public Flowable<BaseResponse> rechargeReceiptUpload(ExReceiptUploadRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.post(APIManager.DEPOSIT_RECHARGERECEIPTUPLOAD_URL,map).map(new Function<ResponseBody, BaseResponse>() {
            @Override
            public BaseResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse>() {
                        });
            }
        });
    }

    @Override
    public Flowable<ExRechargeOrderCheckResponse> rechargeOrderCheck(ExRechargeOrderCheckRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.DEPOSIT_RECHARGEORDERCHECK_URL,map).map(new Function<ResponseBody, ExRechargeOrderCheckResponse>() {
            @Override
            public ExRechargeOrderCheckResponse apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<ExRechargeOrderCheckResponse>() {
                        });
            }
        });
    }
}
